package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.SQLException;
import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class StringMatcher extends TokenMatcher {

	@Override
	public Token match(CharStream stream) {
		if (stream.isEof())
			return null;
		int start = stream.position();
		String result = evalString(stream);
		return result != null ? new Token(result, TokenKind.STRING, start, stream.position()) : null;
	}

	public String evalString(CharStream stream) {
		if (stream.get() == '\'') {
			StringBuffer sb = new StringBuffer();
			while (stream.next()) {
				if (stream.get() == '\'') {
					if (stream.next() && stream.get() == '\'')
						sb.append('\'');
					else
						return sb.toString();
				} else {
					sb.append(stream.get());
				}
			}
			throw new SQLException("SQL语法错误, 未结束的字符串");
		}
		return null;
	}
}
