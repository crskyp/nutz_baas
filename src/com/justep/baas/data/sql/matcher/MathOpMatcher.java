package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class MathOpMatcher extends TokenMatcher{

	@Override
	public Token match(CharStream stream) {
		if(stream.isEof()) return null;
		char ch = stream.get();
		int start = stream.position();
		stream.next();
		
		switch (ch) {
		case '+' :
			return new Token("+",TokenKind.MATH_OP_ADD, start, start+1);
		case '-' :
			return new Token("-",TokenKind.MATH_OP_MINUS, start , start+1);
		case '*' :
			return new Token("*",TokenKind.MATH_OP_MULI, start, start+1);
		case '/' :
			return new Token("/",TokenKind.MATH_OP_DIVIDE, start, start+1);
		default :
			return null;
		}
	}
	
	public static MathOpMatcher createAddMinusMatcher(){
		return new MathOpMatcher(){
			@Override
			public Token match(CharStream stream) {
				if(stream.isEof()) return null;
				char ch = stream.get();
				int start = stream.position();
				switch (ch) {
				case '+' :
					stream.next();
					return new Token("+",TokenKind.MATH_OP_ADD, start, start+1);
				case '-' :
					stream.next();
					return new Token("-",TokenKind.MATH_OP_MINUS, start , start+1);
				default :
					return null;
				}
			}
		};
	}
	public static MathOpMatcher createMuliDivMatcher(){
		return new MathOpMatcher(){
			@Override
			public Token match(CharStream stream) {
				if(stream.isEof()) return null;
				char ch = stream.get();
				int start = stream.position();
				switch (ch) {
				case '*' :
					stream.next();
					return new Token("*",TokenKind.MATH_OP_MULI, start, start+1);
				case '/' :
					stream.next();
					return new Token("/",TokenKind.MATH_OP_DIVIDE, start , start+1);
				default :
					return null;
				}
			}
		};
	}
}
