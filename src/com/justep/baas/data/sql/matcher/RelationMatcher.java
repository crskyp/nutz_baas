package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class RelationMatcher extends TokenMatcher {

	@Override
	public Token match(CharStream stream) {
		if (stream.isEof())
			return null;
		int start = stream.position();
		String wd = stream.nextWord();
		if (wd == null || stream.isEof())
			return null;

		if (stream.get() == '.') {
			StringBuffer sb = new StringBuffer();
			sb.append(wd);
			sb.append('.');
			if (!stream.next())
				return null;
			if ((wd = stream.nextWord()) == null) {
				//is * 
				if (stream.get() == '*') {
					stream.next();
					sb.append('*');
					return new Token(sb.toString(), TokenKind.RELA_ALL, start, stream.position());
				}
			} else {
				sb.append(wd);
				return new Token(sb.toString(), TokenKind.RELA_ONE, start, stream.position());
			}

		}

		return null;
	}

	public static RelationMatcher createOneRelationMatcher() {
		return new RelationMatcher() {
			@Override
			public Token match(CharStream stream) {
				if (stream.isEof())
					return null;
				int start = stream.position();
				String wd = stream.nextWord();
				if (wd == null || stream.isEof())
					return null;

				if (stream.get() == '.') {
					StringBuffer sb = new StringBuffer();
					sb.append(wd);
					sb.append('.');
					if (!stream.next())
						return null;
					if ((wd = stream.nextWord()) == null) {
						if (stream.get() == '*')
							return null;
					} else {
						sb.append(wd);
						return new Token(sb.toString(), TokenKind.RELA_ONE, start, stream.position());
					}

				}

				return null;
			}
		};
	}

	public static RelationMatcher createAllRelationMatcher() {
		return new RelationMatcher() {
			@Override
			public Token match(CharStream stream) {
				if (stream.isEof())
					return null;
				int start = stream.position();
				String wd = stream.nextWord();
				if (wd == null || stream.isEof())
					return null;

				if (stream.get() == '.') {
					if (!stream.next())
						return null;
					if (stream.get() == '*') {
						stream.next();
						return new Token(wd + ".*", TokenKind.RELA_ALL, start, stream.position());
					}
				}

				return null;
			}
		};
	}
}
