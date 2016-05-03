package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class IdentiferMatcher extends TokenMatcher{
	@Override
	public Token match(CharStream stream) {
		if(stream.isEof()) return null;
		int start = stream.position();
		String wd = stream.nextWord();
		
		if(wd != null){
			if(KeywordMatcher.keywords.containsKey(wd.toUpperCase()))
				return null;
			return new Token(wd,TokenKind.IDENTIFIER,start, stream.position());
		}
		return null;
	}

}
