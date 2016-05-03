package com.justep.baas.data.sql.matcher;

import java.util.HashMap;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class MathCmpMatcher extends TokenMatcher{
	private static HashMap<String, TokenKind> cmps = new HashMap<String, TokenKind>();
	static{
		cmps.put("=", TokenKind.MATH_CMP_EQ);
		cmps.put(">", TokenKind.MATH_CMP_GT);
		cmps.put("<", TokenKind.MATH_CMP_LT);
		cmps.put(">=", TokenKind.MATH_CMP_GE);
		cmps.put("<=", TokenKind.MATH_CMP_LE);
		cmps.put("<>", TokenKind.MATH_CMP_NE);
//		cmps.put("!=", TokenKind.MATH_CMP_NE);
	}

	@Override
	public Token match(CharStream stream) {
		if(stream.isEof()) return null;
		int start = stream.position();
		if(isCmpChar(stream.get())){
			String cmp = String.valueOf(stream.get());
			if(stream.next() && isCmpChar(stream.get())){
				cmp += stream.get();
				stream.next();
			}
			if(cmps.containsKey(cmp)){
//				if("!=".equals(cmp)) cmp = "<>";
				return new Token(cmp, cmps.get(cmp), start, stream.position());
			}
		}
		return null;
	}
	
	private boolean isCmpChar(char ch){
		return ch == '=' || ch == '<' || ch == '>' || ch == '!';
	}
	
	public static MathCmpMatcher createEqMatcher(){
		return new MathCmpMatcher(){
			public Token match(CharStream stream) {
				if(stream.isEof()) return null;
				if(stream.get() == '='){
					stream.next();
					return new Token("=", TokenKind.MATH_CMP_EQ	, stream.position() -1 , stream.position());
				}
				return null;
			}
			
		};
	}
	
	

}
