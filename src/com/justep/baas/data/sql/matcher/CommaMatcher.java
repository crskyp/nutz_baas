package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class CommaMatcher extends TokenMatcher{

	@Override
	public Token match(CharStream stream) {
		if(stream.isEof()) return null;
		if(stream.get() == ','){
			stream.next();
			return new Token(",",TokenKind.COMMA, stream.position()-1, stream.position());
		}
		return null;
	}
	

}
