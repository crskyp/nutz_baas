package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;
import com.justep.baas.data.sql.SQLException;

public class ExpStringMatcher extends TokenMatcher {

	@Override
	public Token match(CharStream stream) {
		if (stream.isEof())
			return null;
		int start = stream.position();
		if (stream.get() == '\"') {
			StringBuffer sb = new StringBuffer();
			while (stream.next()) {
				char ch = stream.get();
				switch (ch) {
				case '\\': {
					if (!stream.next()) {
						throw new SQLException("SQL语法错误, 未结束的字符串");
					}
					ch = stream.get();
					switch (ch) {
					case '\\':
						sb.append('\\');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'r':
						sb.append('\r');
						break;
					case 'n':
						sb.append('\n');
						break;
					case '\"':
						sb.append('\"');
						break;
					default:
						{
							throw new SQLException(String.format("SQL语法错误, 非法的转义字符%s, 位置%d", ch+"", stream.position()));							
						}
					}
					break;
				}
				case '\"':
					stream.next();
					return new Token(sb.toString(), TokenKind.EXP_STRING, start,
							stream.position());
				default:
					sb.append(ch);
				}
			}
			throw new SQLException("SQL语法错误, 未结束的字符串");
		}
		return null;
	}

}
