package com.justep.baas.data;

import java.sql.Types;   

public enum DataType {
	STRING("String"), INTEGER("Integer"), LONG("Long"), FLOAT("Float"), DOUBLE("Double"), DECIMAL("Decimal"), BOOLEAN("Boolean"), DATE("Date"), TIME("Time"), DATETIME("DateTime"), OBJECT("Object"), LIST("List");
	
	private String value;
	private DataType(String type) {
		this.value = type;
	}
	
	public static DataType parse(String str) {
		return com.justep.baas.Utils.isEmptyString(str) ? DataType.STRING : DataType.valueOf(str.toUpperCase()); 
	}

	@Override
	public String toString() {
		return value;
	}
	
	public static DataType parseSQLType(int sqlType) {
		switch (sqlType) {
		case Types.CHAR:
		case Types.NCHAR:
		case Types.VARCHAR:
		case Types.NVARCHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
		case Types.NCLOB:
			return STRING;
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
			return INTEGER;
		case Types.BIGINT:
			return LONG;
		case Types.REAL:
		case Types.FLOAT:
			return FLOAT;
		case Types.DOUBLE:
			return DOUBLE;
		case Types.DECIMAL:
		case Types.NUMERIC:
			return DECIMAL;
		case Types.BIT:
		case Types.BOOLEAN:
			return BOOLEAN;
		case Types.DATE:
			return DATE;
		case Types.TIME:
			return TIME;
		case Types.TIMESTAMP:
			return DATETIME;
		default:
			return STRING;
		}
	}
}

