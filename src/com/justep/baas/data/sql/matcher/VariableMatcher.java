package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class VariableMatcher extends TokenMatcher {

	@Override
	public Token match(CharStream stream) {
		if (stream.isEof())
			return null;
		if (stream.get() == ':') {
			int start = stream.position();
			stream.next();
			String wd = stream.nextWord();
			if (wd != null) {
				return new Token(wd, TokenKind.VARIABLE, start, stream.position());
			}
		}
		return null;
	}

}
