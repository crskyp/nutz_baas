package com.justep.baas.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justep.baas.Utils;
import com.justep.baas.data.Table;
import com.justep.baas.data.Transform;
import com.justep.baas.data.DataUtils;
import com.justep.baas.data.sql.SQLStruct;

public class CRUD {
	public static final String QUERY_MASTER_VAR_NAME = "_sys_master_value_"; 
	public static final String QUERY_TREE_PARENT_VAR_NAME = "_sys_tree_parent_value_"; 
	public static final String VARIABLE_FLAG = "var-"; 
	
	//var-开头的参数认为也是变量,variables优先级高于var-
	private static JSONObject getVariables(JSONObject params){
		JSONObject variables = params.getJSONObject("variables");
		if(null==variables) variables = new JSONObject();
		for(String key : params.keySet()){
			if(key.startsWith(VARIABLE_FLAG)){
				String varName = key.substring(VARIABLE_FLAG.length());
				if(!variables.containsKey(varName))variables.put(varName, params.get(key));
			}
		}
		return variables;
	}
	
	//通用的sql查询
	public static JSONObject sqlQuery(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		String db = params.getString("db");
		String sql = params.getString("sql");
		String countSql = params.getString("countSql");
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		if(Utils.isEmptyString(params.getString("filter"))) params.put("filter", "1=1");
		String orderBy = params.getString("orderBy");
		if(Utils.isNotEmptyString(orderBy)) params.put("orderBy", "order by "+orderBy);
		JSONObject variables = getVariables(params);
		Table table = null;
		Connection conn = context.getConnection(db);
		try {
			//进行名字变量转换
			SQLStruct sqlStruct = new SQLStruct(sql);
			table = DataUtils.queryData(conn, sqlStruct.getSQL(params), sqlStruct.getBinds(variables), columns, offset, limit);
			if (offset != null && offset.equals(0)) {
				//where部分进行名字变量转换
				SQLStruct countSqlStruct = new SQLStruct(countSql);
				Object total = DataUtils.getValueBySQL(conn, countSqlStruct.getSQL(params), countSqlStruct.getBinds(variables));
				table.setTotal(Integer.parseInt(total.toString()));
			}
			
			return Transform.tableToJson(table);
		} finally {
			conn.close();
		}
	}
	
	/*
	 * 支持sql中使用名字变量，即：":name"形式
	 */
	public static JSONObject query(JSONObject params, ActionContext context) throws SQLException, NamingException {
		// 获取参数
		String db = params.getString("db");
		String tableName = params.getString("tableName");
		Object columns = params.get("columns");
		Integer limit = params.getInteger("limit");
		Integer offset = params.getInteger("offset");
		String orderBy = params.getString("orderBy");
		String condition = params.getString("condition");
		String filter = params.getString("filter");
		JSONObject variables = getVariables(params);
		List<String> filters = new ArrayList<String>();
		if (!Utils.isEmptyString(condition)) {
			filters.add(condition);
		}
		if (!Utils.isEmptyString(filter)) {
			filters.add(filter);
		}
		//处理主从
		if (params.containsKey("master")) {
			JSONObject master = params.getJSONObject("master");
			if (master.containsKey("field")) {
				filters.add(master.getString("field") + " = :" + QUERY_MASTER_VAR_NAME);
				variables.put(QUERY_MASTER_VAR_NAME, master.get("value"));
			}
		}
		//树形数据
		boolean isTree = false;
		boolean treeDelayLoad = true;
		String parentField = null, idField=null;
		if (params.containsKey("tree")) {
			isTree = true;
			JSONObject treeOption = params.getJSONObject("tree");
			treeDelayLoad = treeOption.getBoolean("isDelayLoad");
			parentField = treeOption.getString("parentField");
			idField = treeOption.getString("idField");
			if(treeDelayLoad){
				//分级加载增加根据父的过滤条件
				String rootFilter = treeOption.containsKey("rootFilter")?treeOption.getString("rootFilter"):null;
				Object parentValue = treeOption.containsKey("parentValue")?treeOption.get("parentValue"):null;
				if(null!=parentValue){
					filters.add(parentField + " = :" + QUERY_TREE_PARENT_VAR_NAME);
					variables.put(QUERY_TREE_PARENT_VAR_NAME, parentValue);
				}else{
					if(null==rootFilter) filters.add(parentField + " is null");//默认根条件就是 parent is null
					else filters.add(rootFilter);
				}
			}
		}
		
		//limit处理，-1取全部
		if(null!=limit && limit==-1) limit = null;

		Table table = null;
		Connection conn = context.getConnection(db);
		try {
			String format = "SELECT %s FROM %s %s %s ";
			
			String where = (filters != null && filters.size() > 0) ? " WHERE " + DataUtils.arrayJoin(filters.toArray(), "(%s)", " AND ") : "";
			orderBy = !Utils.isEmptyString(orderBy) ? " ORDER BY " + orderBy : "";
			
			String sql = String.format(format, "*", tableName, where, orderBy);
			//进行名字变量转换
			SQLStruct sqlStruct = new SQLStruct(sql);
			table = DataUtils.queryData(conn, sqlStruct.getSQL(), sqlStruct.getBinds(variables), columns, offset, limit);
			if (offset != null && offset.equals(0)) {
				//where部分进行名字变量转换
				SQLStruct sqlWhereStruct = new SQLStruct(where);
				String sqlTotal = String.format(format, "COUNT(*)", tableName, sqlWhereStruct.getSQL(), "");
				Object total = DataUtils.getValueBySQL(conn, sqlTotal, sqlWhereStruct.getBinds(variables));
				table.setTotal(Integer.parseInt(total.toString()));
			}
			
			if(isTree && !treeDelayLoad){
				table.setIDColumn(idField);
				return Transform.tableToTreeJson(table, parentField);
			}else
				return Transform.tableToJson(table);
		} finally {
			conn.close();
		}
	}

	private static boolean canSave(JSONObject permissions, String tableName){
		return null==permissions || (null!=permissions && permissions.containsKey(tableName));
	}
	
	private static String getSaveColumnsByPermissions(JSONObject permissions, String tableName){
		String ret = null;
		if(null!=permissions){
			if(permissions.containsKey(tableName))
				ret = permissions.getString(tableName);
		}
		return ret;
	}
	
	public static JSONObject save(JSONObject params, ActionContext context) throws SQLException, NamingException, ParseException {
		// 获取参数
		String db = params.getString("db");
		JSONArray tables = params.getJSONArray("tables");
		JSONObject permissions = params.containsKey("permissions")?params.getJSONObject("permissions"):null;

		Connection conn = context.getConnection(db);
		try {
			conn.setAutoCommit(false);
			if (tables != null && tables.size()>0) {
				for(Object jsonTable : tables){
					Table table = Transform.jsonToTable((JSONObject)jsonTable);
					String tableName = table.getTableName();
					if(canSave(permissions, tableName)) DataUtils.saveData(conn, table, getSaveColumnsByPermissions(permissions, tableName));
				}
			}
			return null;
		} finally {
			conn.close();
		}
	}
}
