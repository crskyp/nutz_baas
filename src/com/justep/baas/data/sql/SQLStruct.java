package com.justep.baas.data.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;
import com.justep.baas.data.sql.token.TokenManager;

public class SQLStruct {
	private String SQL;
	private List<Token> tokens;
	private boolean isMatch = false;
	
	public SQLStruct(String sql){
		this.SQL = sql;
	}
	
	public List<Object> getBinds(Map<String, Object> varsMap){
		if(null!=varsMap){
			List<Object> ret = new ArrayList<Object>();
			List<Token> tokens = getTokens();
			for(Token token : tokens){
				if(TokenKind.VARIABLE == token.getKind()){
					String name = token.getImage();
					if(varsMap.containsKey(name)) ret.add(varsMap.get(name));
					else ret.add(null);//没有变量默认给null
				}
			}
			return ret;
		}
		return null;
	}
	
	public String getSQL(){
		return getSQL(null);
	}
	
	public String getSQL(Map<String,?> params){
		StringBuffer ret = new StringBuffer();
		List<Token> tokens = getTokens();
		for(Token token : tokens){
			TokenKind tokenKind = token.getKind();
			if(TokenKind.EOF != tokenKind){
				if(TokenKind.STRING == tokenKind){
					ret.append("'");
					ret.append(token.getImage()); 
					ret.append("' ");
				}else if(TokenKind.VARIABLE == tokenKind){
					ret.append("?");
					ret.append(" ");
				}else if(TokenKind.SQL_STRING_VARIABLE == tokenKind){
					if(null!=params){
						String wd = token.getImage();
						Object value = params.containsKey(wd)?params.get(wd):"";
						if(null!=value) ret.append(value.toString());
						else ret.append("");
					}else ret.append("");
				}else if(TokenKind.L_BRACKET == tokenKind){
					ret.append(token.getImage());
				}else{
					ret.append(token.getImage());
					ret.append(" ");
				}
			}
		}
		return ret.toString();
	}
	
	private List<Token> getTokens(){
		if(!isMatch){
			tokens = TokenManager.match(SQL);
			isMatch = true;
		}
		return tokens;
	}

	public static void main(String[] args) {
		SQLStruct sql = new SQLStruct("SELECT user.fID, user.fName, user.fPhoneNumber, user.fAddress, COUNT(ord.fID) AS orderCount FROM takeout_user user LEFT JOIN takeout_order ord ON user.fID = ord.fUserID WHERE (::str) ::abc and (0=:useSearch) or (user.fID LIKE :search OR user.fName LIKE :search OR user.fPhoneNumber LIKE :search OR user.fAddress LIKE :search) GROUP BY user.fID, user.fName, user.fPhoneNumber, user.fAddress");
		Map<String,Object>p = new java.util.HashMap<String, Object>();
		p.put("str", "1=2");
		System.out.println(sql.getSQL(p));
		sql = new SQLStruct(null);
		System.out.println(sql.getSQL());
	}
}
