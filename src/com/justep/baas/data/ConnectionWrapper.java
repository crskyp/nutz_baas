package com.justep.baas.data;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionWrapper implements Connection {
	protected Connection conn = null;

	protected ConnectionWrapper(Connection con) {
		conn = con;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return conn.isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return conn.unwrap(arg0);
	}

	@Override
	public void abort(Executor arg0) throws SQLException {
		conn.abort(arg0);		
	}

	@Override
	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		return conn.createArrayOf(arg0, arg1);
	}

	@Override
	public Blob createBlob() throws SQLException {
		return conn.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {
		return conn.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return conn.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return conn.createSQLXML();
	}

	@Override
	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		return conn.createStruct(arg0, arg1);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return conn.getClientInfo();
	}

	@Override
	public String getClientInfo(String arg0) throws SQLException {
		return conn.getClientInfo(arg0);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return conn.getNetworkTimeout();
	}

	@Override
	public String getSchema() throws SQLException {
		return conn.getSchema();
	}

	@Override
	public boolean isValid(int arg0) throws SQLException {
		return conn.isValid(arg0);
	}

	@Override
	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		conn.setClientInfo(arg0);
	}

	@Override
	public void setClientInfo(String arg0, String arg1) throws SQLClientInfoException {
		conn.setClientInfo(arg0, arg1);
	}

	@Override
	public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
		conn.setNetworkTimeout(arg0, arg1);
	}

	@Override
	public void setSchema(String arg0) throws SQLException {
		conn.setSchema(arg0);
	}


	@Override
	public void clearWarnings() throws SQLException {
		conn.clearWarnings();
	}


	@Override
	public void close() throws SQLException {
		conn.close();
	}


	@Override
	public void commit() throws SQLException {
		conn.commit();
	}


	@Override
	public Statement createStatement() throws SQLException {
		return conn.createStatement();
	}


	@Override
	public Statement createStatement(int arg0, int arg1) throws SQLException {
		return conn.createStatement(arg0, arg1);
	}


	@Override
	public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException {
		return conn.createStatement(arg0, arg1, arg2);
	}


	@Override
	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}


	@Override
	public String getCatalog() throws SQLException {
		return conn.getCatalog();
	}


	@Override
	public int getHoldability() throws SQLException {
		return conn.getHoldability();
	}


	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}


	@Override
	public int getTransactionIsolation() throws SQLException {
		return conn.getTransactionIsolation();
	}


	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return conn.getTypeMap();
	}


	@Override
	public SQLWarning getWarnings() throws SQLException {
		return conn.getWarnings();
	}


	@Override
	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}


	@Override
	public boolean isReadOnly() throws SQLException {
		return conn.isReadOnly();
	}


	@Override
	public String nativeSQL(String arg0) throws SQLException {
		return conn.nativeSQL(arg0);
	}


	@Override
	public CallableStatement prepareCall(String arg0) throws SQLException {
		return conn.prepareCall(arg0);
	}


	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2) throws SQLException {
		return conn.prepareCall(arg0, arg1, arg2);
	}


	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3) throws SQLException {
		return conn.prepareCall(arg0, arg1, arg2, arg3);
	}


	@Override
	public PreparedStatement prepareStatement(String arg0) throws SQLException {
		return conn.prepareStatement(arg0);
	}


	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException {
		return conn.prepareStatement(arg0, arg1);
	}


	@Override
	public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException {
		return conn.prepareStatement(arg0, arg1);
	}


	@Override
	public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException {
		return conn.prepareStatement(arg0, arg1);
	}


	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2) throws SQLException {
		return conn.prepareStatement(arg0, arg1, arg2);
	}


	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3) throws SQLException {
		return conn.prepareStatement(arg0, arg1, arg2, arg3);
	}


	@Override
	public void releaseSavepoint(Savepoint arg0) throws SQLException {
		conn.releaseSavepoint(arg0);
	}


	@Override
	public void rollback() throws SQLException {
		conn.rollback();
	}


	@Override
	public void rollback(Savepoint arg0) throws SQLException {
		conn.rollback(arg0);
	}


	@Override
	public void setAutoCommit(boolean arg0) throws SQLException {
		conn.setAutoCommit(arg0);
	}


	@Override
	public void setCatalog(String arg0) throws SQLException {
		conn.setCatalog(arg0);
	}


	@Override
	public void setHoldability(int arg0) throws SQLException {
		conn.setHoldability(arg0);
	}


	@Override
	public void setReadOnly(boolean arg0) throws SQLException {
		conn.setReadOnly(arg0);
	}


	@Override
	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}


	@Override
	public Savepoint setSavepoint(String arg0) throws SQLException {
		return conn.setSavepoint(arg0);
	}


	@Override
	public void setTransactionIsolation(int arg0) throws SQLException {
		conn.setTransactionIsolation(arg0);
	}


	@Override
	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		conn.setTypeMap(arg0);
	}

}
