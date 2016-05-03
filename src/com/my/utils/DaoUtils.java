package com.my.utils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

import com.justep.baas.action.ActionContext;

public class DaoUtils {
	private static Map<String, Dao> map = new HashMap<String, Dao>();
	private static Map<String, Dao> baasDao = new HashMap<String, Dao>();

	/**
	 * 得到dao
	 * 
	 * @param dataSource
	 * @return
	 */
	public static Dao getDao(String dataSource) {
		if (dataSource == null) {
			throw new RuntimeException("dataSource不能为空！");
		}

		Dao dao = map.get(dataSource);
		if (null == dao) {
			synchronized (map) {
				dao = map.get(dataSource);
				if (null == dao) {
					Context context;
					try {
						context = new InitialContext();
						DataSource ds = (DataSource) context.lookup(dataSource);
						dao = new NutDao(ds);
						map.put(dataSource, dao);
					} catch (NamingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return dao;
	}
	
	/**
	 * 在baas事务中，不需要关心connection
	 * @param dataSource
	 * @return
	 */
	public static Dao getDaoInTransaction(String dataSource){
		if (dataSource == null) {
			throw new RuntimeException("dataSource不能为空！");
		}

		Dao dao = baasDao.get(dataSource);
		if (null == dao) {
			synchronized (baasDao) {
				dao = baasDao.get(dataSource);
				if (null == dao) {
					dao = new NutDao(new X5DataSource(dataSource));
					baasDao.put(dataSource, dao);
				}
			}
		}
		return dao;
	}
	
	private static class X5DataSource implements DataSource {
		private String ds;

		public X5DataSource(String dataSource) {
			this.ds =dataSource;
		}

		@Override
		public PrintWriter getLogWriter() throws SQLException {
			return null;
		}

		@Override
		public int getLoginTimeout() throws SQLException {
			return 0;
		}

		@Override
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			return null;
		}

		@Override
		public void setLogWriter(PrintWriter arg0) throws SQLException {

		}

		@Override
		public void setLoginTimeout(int arg0) throws SQLException {

		}

		@Override
		public boolean isWrapperFor(Class<?> arg0) throws SQLException {
			return false;
		}

		@Override
		public <T> T unwrap(Class<T> arg0) throws SQLException {
			return null;
		}

		@Override
		public Connection getConnection() throws SQLException {
			try {
				return ActionContext.getCurrentActionContext().getConnection(ds);
			} catch (NamingException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public Connection getConnection(String arg0, String arg1) throws SQLException {
			return null;
		}

	}

}
