package com.justep.baas.data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Row {
	private Map<String, ColumnValue> values = new HashMap<String, ColumnValue>();
	private RowState state = RowState.NONE;
	
	public Row(Map<String, ColumnValue> values, RowState state) {
		this.values.putAll(values);
		this.state = state;
	}
	
	public RowState getState() {
		return state;
	}
	
	public ColumnValue getColumnValue(String column) {
		return values.containsKey(column)?values.get(column):null;
	}
	
	public Object getValue(String column) {
		ColumnValue cv = getColumnValue(column);
		return null!=cv?cv.getValue():null;
	}

	public Object getOldValue(String column) {
		ColumnValue cv = getColumnValue(column);
		return null!=cv?cv.getOldValue():null;
	}

	public boolean isChanged(String column) {
		ColumnValue cv = getColumnValue(column);
		return null!=cv?cv.isChanged():false;
	}

	public String getString(String column) {
		return (String) getValue(column);
	}

	public Integer getInteger(String column) {
		return (Integer) getValue(column);
	}

	public Float getFloat(String column) {
		return (Float) getValue(column);
	}

	public BigDecimal getDecimal(String column) {
		return (BigDecimal) getValue(column);
	}

	public Boolean getBoolean(String column) {
		return (Boolean) getValue(column);
	}

	public java.sql.Date getDate(String column) {
		return (java.sql.Date) getValue(column);
	}

	public java.sql.Time getTime(String column) {
		return (java.sql.Time) getValue(column);
	}

	public java.sql.Timestamp getDateTime(String column) {
		return (java.sql.Timestamp) getValue(column);
	}

}
