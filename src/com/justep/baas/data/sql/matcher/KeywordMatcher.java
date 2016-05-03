package com.justep.baas.data.sql.matcher;

import java.util.HashMap;

import com.justep.baas.Utils;
import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class KeywordMatcher extends TokenMatcher{
	protected static final HashMap<String, TokenKind> keywords = new HashMap<String, TokenKind>();
	
	static{
		keywords.put("SELECT", TokenKind.KWD_SELECT);
		keywords.put("INSERT", TokenKind.KWD_INSERT);
		keywords.put("DELETE", TokenKind.KWD_DELETE);
		keywords.put("FROM", TokenKind.KWD_FROM);
		keywords.put("INTO", TokenKind.KWD_INTO);
		keywords.put("VALUES", TokenKind.KWD_VALUES);
		keywords.put("WHERE", TokenKind.KWD_WHERE);
		keywords.put("UPDATE", TokenKind.KWD_UPDATE);
		keywords.put("RETRACT", TokenKind.KWD_RETRACT);
		keywords.put("OPTIONAL", TokenKind.KWD_OPTIONAL);
		keywords.put("JOIN", TokenKind.KWD_JOIN);
		keywords.put("SET", TokenKind.KWD_SET);
		keywords.put("ON", TokenKind.KWD_ON);
		keywords.put("GROUP", TokenKind.KWD_GROUP);
		keywords.put("BY", TokenKind.KWD_BY);
		keywords.put("HAVING", TokenKind.KWD_HAVING);
		keywords.put("ORDER", TokenKind.KWD_ORDER);
		keywords.put("ASC", TokenKind.KWD_ASC);
		keywords.put("DESC", TokenKind.KWD_DESC);
		keywords.put("AS", TokenKind.KWD_AS);
		keywords.put("IS", TokenKind.KWD_IS);
		keywords.put("NULL", TokenKind.KWD_NULL);
		keywords.put("AND", TokenKind.KWD_AND);
		keywords.put("OR", TokenKind.KWD_OR);
		keywords.put("NOT", TokenKind.KWD_NOT);
		keywords.put("EXISTS", TokenKind.KWD_EXISTS);
		keywords.put("LIMIT", TokenKind.KWD_LIMIT);
		keywords.put("DISTINCT", TokenKind.KWD_DISTINCT);
		keywords.put("LIKE", TokenKind.KWD_LIKE);
		keywords.put("IN", TokenKind.KWD_IN);
		keywords.put("BETWEEN", TokenKind.KWD_BETWEEN);
		keywords.put("UNION", TokenKind.KWD_UNION);
		keywords.put("CASE", TokenKind.KWD_CASE);
		keywords.put("WHEN", TokenKind.KWD_WHEN);
		keywords.put("THEN", TokenKind.KWD_THEN);
		keywords.put("ELSE", TokenKind.KWD_ELSE);
		keywords.put("END", TokenKind.KWD_END);
		keywords.put("TRUE", TokenKind.KWD_TRUE);
		keywords.put("FALSE", TokenKind.KWD_FALSE);
		keywords.put("CAST", TokenKind.KWD_CAST);
		keywords.put("SQL", TokenKind.KWD_SQL);
	}
	
	private String kwd = null;
	private TokenKind kind = null;
	public KeywordMatcher(String kd){
		Utils.check(keywords.containsKey(kd), "SQL语法错误, 非法的关键字", kd);
		kwd = kd;
		kind = keywords.get(kwd);
	}
	@Override
	public Token match(CharStream stream) {
		if(stream.isEof()) return null;
		int start = stream.position();
		String wd = stream.nextWord();
		if(wd == null) return null;
		wd = wd.toUpperCase();
		if(kwd.equals(wd)){
			return new Token(wd,kind,start, stream.position());
		}
		return null;
	}
	
	public static boolean isKeyword(String wd){
		return Utils.isEmptyString(wd) ? false : keywords.containsKey(wd.toUpperCase());
	}

}
