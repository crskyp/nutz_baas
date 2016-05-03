package com.justep.baas.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table {
	private Map<String, DataType> columns = new LinkedHashMap<String, DataType>();
	private String idColumn = null;
	private Integer total = null;
	private Integer offset = null;
	private String tableName = null;
	
	private List<Row> rows = new ArrayList<Row>();

	public Table(Map<String, DataType> columns) {
		this.columns.putAll(columns);
	}
	
	public Collection<String> getColumnNames() {
		return columns.keySet();
	}
	
	public Collection<DataType> getColumnTypes() {
		return columns.values();
	}
	
	public DataType getColumnType(String column) {
		return columns.get(column);
	}
	
	public void appendRow(Row row) {
		rows.add(row);
	}
	
	public void appendRows(Collection<Row> rows) {
		for (Row row : rows) {
			appendRow(row);
		}
	}
	
	public List<Row> getRows() {
		return rows;
	}
	
	public Collection<Row> getRows(RowState state) {
		List<Row> result = new ArrayList<Row>();
		for (Row row : rows) {
			if (state.equals(row.getState())) {
				result.add(row);
			}
		}
		return result;
	}
	
	public String getIDColumn() {
		return idColumn;
	}
	
	public void setIDColumn(String idColumn) {
		this.idColumn = idColumn;
	}
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
