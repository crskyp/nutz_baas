package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class FunctionMatcher extends TokenMatcher {

	@Override
	public Token match(CharStream stream) {
		if (stream.isEof())
			return null;
		int start = stream.position();
		String wd = stream.nextWord();
		if (wd == null || stream.isEof())
			return null;

		if (stream.get() == '(') {
			StringBuffer sb = new StringBuffer();
			sb.append(wd);
			sb.append('(');
			if (!stream.next())
				return null;
			while(!stream.isEof()){
				char c = stream.get();
				sb.append(c);
				stream.next();
				if(c == ')') break;
				else{
					wd = stream.nextWord();
					if(null!=wd){
						sb.append(wd);
					}
				}
			}
			return new Token(sb.toString(), TokenKind.FUNCTION, start, stream.position());
		}

		return null;
	}
}
