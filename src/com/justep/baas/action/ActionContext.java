package com.justep.baas.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.data.DataUtils;
import com.justep.baas.data.ConnectionWrapper;

public class ActionContext {
	private static ThreadLocal<ActionContext> tl = new ThreadLocal<ActionContext>();

	public static final String REQUEST = "request";
	public static final String RESPONSE = "response";

	private JSONObject DBConfig;
	private Map<String, Object> values = new HashMap<String, Object>();
	private Map<String, CoreConnection> connections = new HashMap<String, CoreConnection>();

	public ActionContext(JSONObject DBConfig) {
		this.DBConfig = DBConfig;
		tl.set(this);
	}
	
	public static ActionContext getCurrentActionContext(){
		return tl.get();
	}
	
	// 获取一个受控连接
	public Connection getConnection(String datasource) throws SQLException, NamingException {
		//wjw,add
		if (tl.get() == null)
			tl.set(this);

		CoreConnection conn = connections.get(datasource);
		if (null == conn) {
			String name = datasource;
			if (null != DBConfig && DBConfig.containsKey(name))
				name = DBConfig.getString(name);
			conn = new CoreConnection(DataUtils.getConnection(name), datasource);
		}
		return new ActionContextConnection(conn);
	}

	// 获取一个原生连接,需要开发者管理
	public Connection newConnection(String datasource) throws SQLException, NamingException {
		String name = datasource;
		if (null != DBConfig && DBConfig.containsKey(name))
			name = DBConfig.getString(name);
		return DataUtils.getConnection(name);
	}

	public Object get(String key) {
		return values.get(key);
	}

	public void put(String key, Object value) {
		values.put(key, value);
	}

	public void clear() {
		values.clear();
	}

	public boolean containsKey(String key) {
		return values.containsKey(key);
	}

	public void commit() throws SQLException {
		for (String key : connections.keySet()) {
			Connection conn = connections.get(key);
			if (!conn.getAutoCommit())
				conn.commit();
		}
	}

	public void rollback() throws SQLException {
		for (String key : connections.keySet()) {
			Connection conn = connections.get(key);
			if (!conn.getAutoCommit())
				conn.rollback();
		}
	}

	public void closeConnection() throws SQLException {
		for (String key : connections.keySet()) {
			CoreConnection conn = connections.get(key);
			conn.forcedClose();
		}
		// wjw,add
		tl.remove();
	}

	private class CoreConnection extends ConnectionWrapper {
		protected int active = 0;
		private String name;

		protected CoreConnection(Connection con, String name) {
			super(con);
			this.name = name;
			connections.put(name, this);
		}

		// 强制关闭
		void forcedClose() throws SQLException {
			connections.remove(name);
			conn.close();
		}

		@Override
		public void close() throws SQLException {
			if (--active == 0 && conn.getAutoCommit()) {
				forcedClose();
			}
		}

		@Override
		public boolean isClosed() throws SQLException {
			return this.conn == null || this.conn.isClosed();
		}
	}

	private class ActionContextConnection extends ConnectionWrapper {
		private boolean closed = false;

		protected ActionContextConnection(CoreConnection con) {
			super(con);
			con.active++;
		}

		@Override
		public void close() throws SQLException {
			closed = true;
			conn.close();
		}

		@Override
		public boolean isClosed() throws SQLException {
			return closed || this.conn == null || this.conn.isClosed();
		}
	}
}
