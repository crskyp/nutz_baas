package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public abstract class BracketMatcher extends TokenMatcher{


	
	public static BracketMatcher createLMatcher(){
		return new BracketMatcher(){
			@Override
			public Token match(CharStream stream) {
				if((!stream.isEof()) && (stream.get() == '(')) {
					stream.next();
					return new Token("(", TokenKind.L_BRACKET, stream.position() -1, stream.position());
				}
				return null;
			}
		};
	}
	
	public static BracketMatcher createRMatcher(){
		return new BracketMatcher(){
			@Override
			public Token match(CharStream stream) {
				if((!stream.isEof()) && (stream.get() == ')')) {
					stream.next();
					return new Token(")", TokenKind.R_BRACKET, stream.position() -1, stream.position());
				}
				return null;
			}
		};
	}

	public static BracketMatcher createLFMatcher(){
		return new BracketMatcher(){
			@Override
			public Token match(CharStream stream) {
				if((!stream.isEof()) && (stream.get() == '[')) {
					stream.next();
					return new Token("[", TokenKind.L_F, stream.position() -1, stream.position());
				}
				return null;
			}
		};
	}
	
	public static BracketMatcher createRFMatcher(){
		return new BracketMatcher(){
			@Override
			public Token match(CharStream stream) {
				if((!stream.isEof()) && (stream.get() == ']')) {
					stream.next();
					return new Token("]", TokenKind.R_F, stream.position() -1, stream.position());
				}
				return null;
			}
		};
	}
}
