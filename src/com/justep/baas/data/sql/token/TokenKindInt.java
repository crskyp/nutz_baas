package com.justep.baas.data.sql.token;

public class TokenKindInt {
	//kind编码，0-7位 kind值,8-15位小类， 16-25 kind类型
	
	// keyword
	public static final int KEYWORD = 1 << 15;
	public static final int KWD_SELECT = KEYWORD | 1;
	public static final int KWD_INSERT = KEYWORD | 2;
	public static final int KWD_DELETE = KEYWORD | 3;
	public static final int KWD_FROM = KEYWORD | 4;
	public static final int KWD_INTO = KEYWORD | 5;
	public static final int KWD_VALUES = KEYWORD | 6;
	public static final int KWD_WHERE = KEYWORD | 7;
	public static final int KWD_UPDATE = KEYWORD | 8;
	public static final int KWD_RETRACT = KEYWORD | 9;
	public static final int KWD_SET = KEYWORD | 10;
	public static final int KWD_OPTIONAL = KEYWORD | 11;
	public static final int KWD_JOIN = KEYWORD | 12;
	public static final int KWD_ON = KEYWORD | 13;
	public static final int KWD_GROUP = KEYWORD | 14;
	public static final int KWD_BY = KEYWORD | 15;
	public static final int KWD_HAVING = KEYWORD | 16;
	public static final int KWD_ORDER = KEYWORD | 17;
	public static final int KWD_ASC = KEYWORD | 18;
	public static final int KWD_DESC = KEYWORD | 19;
	public static final int KWD_AS = KEYWORD | 20;
	public static final int KWD_IS = KEYWORD | 21;
	public static final int KWD_NULL = KEYWORD | 22;
	public static final int KWD_AND = KEYWORD | 23;
	public static final int KWD_OR = KEYWORD | 24;
	public static final int KWD_NOT = KEYWORD | 25;
	public static final int KWD_EXISTS = KEYWORD | 26;
	public static final int KWD_LIMIT = KEYWORD | 27;
	public static final int KWD_DISTINCT = KEYWORD | 28;
	public static final int KWD_LIKE = KEYWORD | 29;
	public static final int KWD_IN = KEYWORD | 30;
	public static final int KWD_BETWEEN = KEYWORD | 31;
	public static final int KWD_UNION = KEYWORD | 32;
	public static final int KWD_CASE = KEYWORD | 33;
	public static final int KWD_WHEN = KEYWORD | 34;
	public static final int KWD_THEN = KEYWORD | 35;
	public static final int KWD_ELSE = KEYWORD | 36;
	public static final int KWD_END = KEYWORD | 37;
	public static final int KWD_TRUE = KEYWORD | 38;
	public static final int KWD_FALSE = KEYWORD | 39;
	public static final int KWD_CAST = KEYWORD | 40;
	public static final int KWD_SQL = KEYWORD | 41;
	
	

	public static void main(String[] args) {
		System.out.println(Integer.toBinaryString(KWD_SQL));
	}

}
