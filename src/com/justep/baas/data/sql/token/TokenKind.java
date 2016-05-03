package com.justep.baas.data.sql.token;

public enum TokenKind {
	EOF("<EOF>") // END OF FILE

	/// keyword
	, KWD_SELECT("SELECT") //select
	, KWD_INSERT("INSERT")
	, KWD_DELETE("DELETE") //delete
	, KWD_FROM("FROM") // from
	, KWD_INTO("INTO")
	, KWD_VALUES("VALUES")
	, KWD_WHERE("WHERE") //where
	, KWD_UPDATE("UPDATE") //assert
	, KWD_RETRACT("RETRACT") //retract
	, KWD_SET("SET") //set
	, KWD_OPTIONAL("OPTIONAL") //optional
	, KWD_JOIN("JOIN") //join
	, KWD_ON("ON") //on
	, KWD_GROUP("GROUP") //group
	, KWD_BY("BY") //by
	, KWD_HAVING("HAVING") //having
	, KWD_ORDER("ORDER") //order
	, KWD_ASC("ASC") //asc
	, KWD_DESC("DESC") //desc
	, KWD_AS("AS") //as
	, KWD_IS("IS") //is
	, KWD_NULL("NULL") //null
	, KWD_AND("AND") //and	 
	, KWD_OR("OR") //or
	, KWD_NOT("NOT") //not
	, KWD_EXISTS("EXISTS") //exists
	, KWD_LIMIT("LIMIT") //limit
	, KWD_DISTINCT("DISTINCT")//distinct
	, KWD_LIKE("LIKE") //like
	, KWD_IN("IN") //in
	, KWD_BETWEEN("BETWEEN") //between
	, KWD_UNION("UNION") //union
	, KWD_CASE("CASE") //case
	, KWD_WHEN("WHEN") //when
	, KWD_THEN("THEN") //then
	, KWD_ELSE("ELSE") //else
	, KWD_END("END") //end
	, KWD_TRUE("TRUE") //TRUE
	, KWD_FALSE("FALSE") //FALSE
	, KWD_CAST("CAST")
	, KWD_SQL("SQL")

	//符号
	, COMMA("<COMMA>") //,
	, L_BRACKET("(") //(
	, R_BRACKET(")") //)
	, L_F("[") //[
	, R_F("]") //]

	//算数操作符
	, MATH_OP_ADD_MINUS("+ -") //+ -
	, MATH_OP_MULI_DIVIDE("* /") // * /
	, MATH_OP_ADD("+") //+
	, MATH_OP_MINUS("-") //-
	, MATH_OP_MULI("*") //*
	, MATH_OP_DIVIDE("/") // /

	//算数比较
	, MATH_CMP("<COMPARE>")
	, MATH_CMP_EQ("=") //=
	, MATH_CMP_GT(">") //>
	, MATH_CMP_LT("<") //<
	, MATH_CMP_GE(">=") //>=
	, MATH_CMP_LE("<=") //<=
	, MATH_CMP_NE("<>") //<>

	//
	, FUNCTION("<Function>") //函数，如:count(*)
	, RELATION("<Relation>") //所有的relation
	, RELA_ALL("<Concept.*>") //RELATION ALL like ind.*
	, RELA_ONE("<Concept.Relation>") //一个relation 如 p1.name
	, INDIVIDUAL("<Individual>")
	, IDENTIFIER("<Identifier>")
	//data type
	, NUMBER("<Number>"), INTEGER("<Integer>"), FLOAT("<Float>"), STRING("<String>"), SQL_STRING_VARIABLE("<SQL.String.Variable>"), VARIABLE("<Variable>")// variable
	, EXP_IDENTIFIER("<Identifier>"), EXP_STRING("<String>"), EXP_TRUE("true"), EXP_FALSE("false"), EXP_AND("and"), EXP_OR("or"), EXP_NOT("not"), EXP_NULL("null")
	, PERM_CONCEPT("<Concept>") //权限中要替换的概念
	, PERM_RELATION("<Concept.Relation>")//权限中的完整关系 a.b
	, PERM_SINGLE_RELATION("<Relation>") //权限中的单纯关系
	, PERM_EXPRESSION("<Expression>") //表达式
	
	;
	private String text = null;
	private TokenKind(String txt){
		text = txt;
	}
	public String toString(){
		return text;
	}
	

}
