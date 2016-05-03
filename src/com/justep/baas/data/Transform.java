package com.justep.baas.data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justep.baas.Utils;

public class Transform {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
	private static final SimpleDateFormat TIME_FORMAT2 = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	private static final String USER_DATA = "userdata";
	private static final String TABLE_NAME = "tableName";
	private static final String ID_COLUMN_NAME = "idColumnName";
	private static final String ID_COLUMN_TYPE = "idColumnType";
	private static final String COLUMN_NAMES = "relationAlias";
	private static final String COLUMN_TYPES = "relationTypes";

	private static final String TABLE_TOTAL = "sys.count";
	private static final String TABLE_OFFSET = "sys.offset";

	private static final String ROWS = "rows";
	private static final String ROW_STATE = "recordState";
	private static final String ROW_ID = "id";
	private static final String ROW_VALUE = "value";
	private static final String ROW_OLD_VALUE = "originalValue";
	private static final String ROW_VALUE_CHANGED = "changed";
	private static final Integer ROW_VALUE_ISCHANGED = 1;

	/**
	 * 将.w中data.toJson()的数据转换为Table
	 * @param json
	 * @return
	 * @throws ParseException
	 */
	public static Table jsonToTable(String json) throws ParseException {
		return jsonToTable((JSONObject) JSONObject.parse(json));
	}

	/**
	 * 将.w中data.toJson()的数据转换为Table
	 * @param json
	 * @return
	 * @throws ParseException
	 */
	public static Table jsonToTable(JSONObject json) throws ParseException {
		JSONObject jsUserData = json.getJSONObject(USER_DATA);

		Map<String, DataType> columns = new LinkedHashMap<String, DataType>();

		String idColumn = jsUserData.getString(ID_COLUMN_NAME);
		String idColumnType = jsUserData.getString(ID_COLUMN_TYPE);
		String tableName = jsUserData.getString(TABLE_NAME);
		if (idColumn != null && idColumnType != null) {
			columns.put(idColumn, DataType.parse(idColumnType));
		}

		String[] names = jsUserData.getString(COLUMN_NAMES).split(",");
		String[] types = jsUserData.getString(COLUMN_TYPES).split(",");
		for (int i = 0, len = names.length; i < len; i++) {
			columns.put(names[i], DataType.parse(types[i]));
		}

		Table table = new Table(columns);

		table.setTableName(tableName);
		if (idColumn != null) {
			table.setIDColumn(idColumn);
		}
		if (jsUserData.containsKey(TABLE_TOTAL)) {
			table.setTotal(jsUserData.getInteger(TABLE_TOTAL));
		}
		if (jsUserData.containsKey(TABLE_OFFSET)) {
			table.setOffset(jsUserData.getInteger(TABLE_OFFSET));
		}

		JSONArray jsRows = json.getJSONArray(ROWS);
		List<Row> rows = new ArrayList<Row>();
		for (int i = 0, len = jsRows.size(); i < len; i++) {
			JSONObject jsRow = jsRows.getJSONObject(i);
			Row row = jsonToRow(table, jsRow);
			rows.add(row);
		}
		table.appendRows(rows);

		return table;
	}

	/**
	 * 将Table转换为.w中data可以加载的JSON数据结构
	 * @param table
	 * @return
	 */
	public static JSONObject tableToJson(Table table) {
		return tableToJson(table, null);
	}
	
	/**
	 * 将Table转换为.w中data可以加载的树形JSON数据结构
	 * @param table 必须指定Table的idColumn属性
	 * @param parentColumn 与Table的idColumn构成父子树
	 * @return
	 */
	public static JSONObject tableToTreeJson(Table table, String parentColumn) {
		if (parentColumn == null || table.getIDColumn() == null) {
			throw new RuntimeException("转换树形结构必须指定Table的idColumn属性和parentColumn参数");
		}
		return tableToJson(table, parentColumn);
	}
	
	private static JSONObject tableToJson(Table table, String parentColumn) {
		JSONObject jsTable = new JSONObject();
		jsTable.put("@type", "table");

		JSONObject jsUserData = new JSONObject();
		jsUserData.put(TABLE_NAME, table.getTableName());
		jsUserData.put(TABLE_TOTAL, table.getTotal());
		jsUserData.put(TABLE_OFFSET, table.getOffset());
		
		String idColumn = table.getIDColumn();
		if (idColumn != null) {
			jsUserData.put(ID_COLUMN_NAME, idColumn);
			jsUserData.put(ID_COLUMN_TYPE, table.getColumnType(idColumn).toString());
		}
		jsUserData.put(COLUMN_NAMES, DataUtils.arrayJoin(table.getColumnNames().toArray(), "%s", ","));
		jsUserData.put(COLUMN_TYPES, DataUtils.arrayJoin(table.getColumnTypes().toArray(), "%s", ","));
		jsTable.put(USER_DATA, jsUserData);

		JSONArray jsRows = new JSONArray();
		if (parentColumn == null) {
			for (Row row : table.getRows()) {
				JSONObject jsRow = rowToJson(table, row);
				jsRows.add(jsRow);
			}
		} else {
			Map<Object, JSONObject> parentMap = new HashMap<Object, JSONObject>();
			Map<Object, List<JSONObject>> childrenMap = new HashMap<Object, List<JSONObject>>();
			for (Row row : table.getRows()) {
				JSONObject jsRow = rowToJson(table, row); 
				
				Object idValue = row.getValue(idColumn);
				Object parentValue = row.getValue(parentColumn);
				
				// find parent
				if (parentMap.containsKey(parentValue)) {
					JSONObject parentRow = parentMap.get(parentValue);
					addJsonRowChild(parentRow, jsRow);
				} else {
					jsRows.add(jsRow);
				}
				// find children
				if (childrenMap.containsKey(idValue)) {
					List<JSONObject> children = childrenMap.get(idValue);
					for (JSONObject childRow : children) {
						addJsonRowChild(jsRow, childRow);
						jsRows.remove(childRow);
					}
				}
				parentMap.put(idValue, jsRow);
				if (parentValue != null) {
					if (!childrenMap.containsKey(parentValue)) {
						childrenMap.put(parentValue, new ArrayList<JSONObject>());
					}
					childrenMap.get(parentValue).add(jsRow);
				}
			}
		}
		jsTable.put(ROWS, jsRows);

		return jsTable;
	}
	
	private static void addJsonRowChild(JSONObject parent, JSONObject child) {
		if (!parent.containsKey(ROWS)) {
			parent.put(ROWS, new JSONArray());
		}
		parent.getJSONArray(ROWS).add(child);
	}
	
	private static Row jsonToRow(Table table, JSONObject jsRow) throws ParseException {
		Map<String, ColumnValue> values = new HashMap<String, ColumnValue>();
		RowState state = RowState.NONE;

		JSONObject jsUserData = jsRow.getJSONObject(USER_DATA);
		if (jsUserData != null) {
			state = RowState.parse(jsUserData.getString(ROW_STATE));
			if (jsUserData.containsKey(ROW_ID)) {
				String idColumn = table.getIDColumn();
				ColumnValue value = jsonToColumnValue(jsUserData.getJSONObject(ROW_ID), table.getColumnType(idColumn));
				values.put(idColumn, value);
			}
		}
		for (String name : table.getColumnNames()) {
			if (jsRow.containsKey(name)) {
				ColumnValue value = jsonToColumnValue(jsRow.getJSONObject(name), table.getColumnType(name));
				values.put(name, value);
			}
		}

		return new Row(values, state);
	}

	private static JSONObject rowToJson(Table table, Row row) {
		JSONObject jsRow = new JSONObject();

		JSONObject jsUserData = new JSONObject();
		jsUserData.put(ROW_STATE, row.getState().toString());
		
		String idColumn = table.getIDColumn();
		if (idColumn != null) {
			jsUserData.put(ROW_ID, columnValueToJson(row.getColumnValue(idColumn), table.getColumnType(idColumn)));
		}

		jsRow.put(USER_DATA, jsUserData);

		for (String name : table.getColumnNames()) {
			jsRow.put(name, columnValueToJson(row.getColumnValue(name), table.getColumnType(name)));
		}

		return jsRow;
	}

	private static ColumnValue jsonToColumnValue(JSONObject jsColumnValue, DataType type) throws ParseException {
		Object value = jsonToValue(jsColumnValue.get(ROW_VALUE), type);
		Object oldValue = jsonToValue(jsColumnValue.get(ROW_OLD_VALUE), type);
		Integer changed = jsColumnValue.getInteger(ROW_VALUE_CHANGED);
		return new ColumnValue(value, oldValue, ROW_VALUE_ISCHANGED.equals(changed));
	}

	private static JSONObject columnValueToJson(ColumnValue value, DataType type) {
		JSONObject jsColumnValue = new JSONObject();
		if (value != null) {
			jsColumnValue.put(ROW_VALUE, valueToJson(value.getValue(), type));
			if (value.isChanged()) {
				jsColumnValue.put(ROW_OLD_VALUE, valueToJson(value.getOldValue(), type));
				jsColumnValue.put(ROW_VALUE_CHANGED, ROW_VALUE_ISCHANGED);
			}
		}
		return jsColumnValue;
	}

	private static Object jsonToValue(Object jsValue, DataType type) throws ParseException {
		if (jsValue == null) {
			return null;
		}
		switch (type) {
		case STRING:
			break;
		case INTEGER:
			return new Integer(jsValue.toString());
		case LONG:
			return new Long(jsValue.toString());
		case FLOAT:
			return new Float(jsValue.toString());
		case DOUBLE:
			return new Double(jsValue.toString());
		case DECIMAL:
			return new BigDecimal(jsValue.toString());
		case BOOLEAN:
			return new Boolean(jsValue.toString());
		case DATE:
			return new java.sql.Date(DATE_FORMAT.parse(jsValue.toString()).getTime());
		case TIME:
			String tv = jsValue.toString();
			return new java.sql.Time((tv.length()>8?TIME_FORMAT.parse(tv):TIME_FORMAT2.parse(tv)).getTime());
		case DATETIME:
			return new java.sql.Timestamp(DATETIME_FORMAT.parse(jsValue.toString()).getTime());
		case OBJECT:	
		case LIST:
			return Utils.isNotEmptyString(jsValue.toString())?JSONObject.parse(jsValue.toString()):null;
		}
		return jsValue;
	}

	public static Object strToValue(String str, DataType type) throws ParseException {
		return jsonToValue(str, type);
	}

	private static Object valueToJson(Object value, DataType type) {
		if (value == null) {
			return null;
		}
		switch (type) {
		case STRING:
		case INTEGER:
		case LONG:
		case FLOAT:
		case DOUBLE:
		case BOOLEAN:
			break;
		case DECIMAL:
			return value.toString();
		case DATE:
			return DATE_FORMAT.format(value);
		case TIME:
			return TIME_FORMAT.format(value);
		case DATETIME:
			return DATETIME_FORMAT.format(value);
		case OBJECT:	
		case LIST:
			return value.toString();
		}
		return value;
	}

	/**
	 * 按ResultSet的列定义，转换ResultSet为Table
	 * @param rs ResultSet
	 * @param columns 指定要返回的列，以逗号分隔，如果为空则返回所有列；columns不仅用于限定返回的列范围，同时也用于指定返回列名的大小写
	 * @param count 行数，如果为null或小于零，则转换所有行
	 * @return
	 * @throws SQLException
	 */
	public static Table resultSetToTable(ResultSet rs, String columns, Integer count) throws SQLException {
		Table table = createTableByResultSet(rs, columns);
		loadRowsFromResultSet(table, rs, count);	
		return table;
	}
	
	/**
	 * 按ResultSet的列定义生成Table
	 * @param rs ResultSet
	 * @param columns 指定要返回的列，以逗号分隔，如果为空则返回所有列；columns不仅可以用于限定返回的列范围，同时也用于指定返回列名的大小写
	 * @return
	 * @throws SQLException
	 */
	public static Table createTableByResultSet(ResultSet rs, String columns) throws SQLException {
		// 构造一个列名的映射，用于忽略列名大小写敏感
		Map<String, String> columnNameMap = null;
		if (columns != null && columns.trim().length() > 0) {
			columnNameMap = new HashMap<String, String>();
			for (String column : columns.split(",")) {
				columnNameMap.put(column.toUpperCase(), column);
			}
		}
		
		ResultSetMetaData meta = rs.getMetaData();
		Map<String, DataType> tableColumns = new LinkedHashMap<String, DataType>();
		for (int i = 1, len = meta.getColumnCount(); i <= len; i++) {
			String name = meta.getColumnLabel(i);
			if (columnNameMap == null || columnNameMap.containsKey(name.toUpperCase())) {
				if (columnNameMap != null) {
					name = columnNameMap.get(name.toUpperCase());
				}
				DataType type = DataType.parseSQLType(meta.getColumnType(i));
				tableColumns.put(name, type);
			}
		}

		return new Table(tableColumns);
	}
	
	/**
	 * 转换ResultSet行数据到Table
	 * @param table
	 * @param rs ResultSet
	 * @param count 行数，如果为null或小于零，则转换所有行
	 * @throws SQLException
	 */
	public static void loadRowsFromResultSet(Table table, ResultSet rs, Integer count) throws SQLException {
		
		// 构造一个列名的映射，用于忽略列名大小写敏感
		Map<String, String> columnNameMap = new HashMap<String, String>();
		for (String column : table.getColumnNames()) {
			columnNameMap.put(column.toUpperCase(), column);
		}
		// 
		Map<String, DataType> columns = new HashMap<String, DataType>();
		ResultSetMetaData meta = rs.getMetaData();
		for (int i = 1, len = meta.getColumnCount(); i <= len; i++) {
			String name = meta.getColumnLabel(i);
			if (columnNameMap.containsKey(name.toUpperCase())) {
				name = columnNameMap.get(name.toUpperCase());
				DataType type = table.getColumnType(name);
				columns.put(name, type);
			}
		}

		int i = 0;
		while (rs.next() && (count == null || count < 0 || i < count)) {
			table.appendRow(resultSetToRow(table, rs, RowState.NONE, columns));
			i++;
		}
	}

	private static Row resultSetToRow(Table table, ResultSet rs, RowState state, Map<String, DataType> columns) throws SQLException {
		Map<String, ColumnValue> values = new HashMap<String, ColumnValue>();
		for (String name : columns.keySet()) {
			DataType type = columns.get(name);
			Object value = null;
			switch(type) {
			case STRING:
				value = rs.getString(name);
				break;
			case INTEGER:
				value = rs.getObject(name) == null ? null : rs.getInt(name);
				break;
			case LONG:
				value = rs.getObject(name) == null ? null : rs.getLong(name);
				break;
			case FLOAT:
				value = rs.getObject(name) == null ? null : rs.getFloat(name);
				break;
			case DOUBLE:
				value = rs.getObject(name) == null ? null : rs.getDouble(name);
				break;
			case DECIMAL:
				value = rs.getBigDecimal(name);
				break;
			case BOOLEAN:
				value = rs.getBoolean(name);
				break;
			case DATE:
				value = rs.getDate(name);
				break;
			case TIME:
				value = rs.getTime(name);
				break;
			case DATETIME:
				value = rs.getTimestamp(name);
				break;
			default:
				value = rs.getObject(name);
			}
			values.put(name, new ColumnValue(value));
		}
		return new Row(values, state);
	};
	
	/**
	 * 按来自前端Data组件的列定义，转换ResultSet为Table
	 * @param rs ResultSet
	 * @param columnsDefine 来自前端Data的列定义
	 * @param count 行数，如果为null或小于零，则转换所有行
	 * @return
	 * @throws SQLException
	 */
	public static Table resultSetToTable(ResultSet rs, JSONObject columnsDefine, Integer count) throws SQLException {
		Table table = createTableByColumnsDefine(columnsDefine);
		loadRowsFromResultSet(table, rs, count);	
		return table;
	}
	
	/**
	 * 按来自前端Data组件的列定义生成Table对象
	 * @param columnsDefine
	 * @return
	 */
	public static Table createTableByColumnsDefine(JSONObject columnsDefine) {
		Map<String, DataType> columns = new LinkedHashMap<String, DataType>();
		for (String key : columnsDefine.keySet()) {
			JSONObject columnDefine = columnsDefine.getJSONObject(key);
			columns.put(columnDefine.getString("name"), DataType.parse(columnDefine.getString("type")));
		}
		Table table = new Table(columns);
		return table;
	}
	
}
