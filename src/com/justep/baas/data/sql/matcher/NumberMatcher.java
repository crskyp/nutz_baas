package com.justep.baas.data.sql.matcher;

import com.justep.baas.data.sql.token.CharStream;
import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;
import com.justep.baas.data.sql.token.Utilities;

public class NumberMatcher extends TokenMatcher {
	private static final int SIGN = 0x08;

	private static final int INT = 0x04;

	private static final int DOT = 0x02;

	private static final int DEC = 0x01;

	private static TokenKind[] kinds = new TokenKind[16];
	static {
		kinds[SIGN | INT] = TokenKind.INTEGER;
		kinds[INT] = TokenKind.INTEGER;

		kinds[SIGN | INT | DOT | DEC] = TokenKind.FLOAT;
		kinds[SIGN | DOT | DEC] = TokenKind.FLOAT;
		kinds[SIGN | INT | DOT] = TokenKind.FLOAT;
		kinds[INT | DOT | DEC] = TokenKind.FLOAT;
		kinds[INT | DOT] = TokenKind.FLOAT;
		kinds[DOT | DEC] = TokenKind.FLOAT;
	}

	@Override
	public Token match(CharStream stream) {
		if (stream.isEof())
			return null;
		int start = stream.position();
		int state = 0;
		StringBuffer sb = new StringBuffer();
		if (Utilities.isSign(stream.get())) {
			state |= SIGN;
			sb.append(stream.get());
			if (!stream.next())
				return null;
		}

		char ch = stream.get();
		if (Utilities.isDigit(ch)) {
			state |= INT;
			sb.append(ch);
			while (stream.next() && Utilities.isDigit(ch = stream.get()))
				sb.append(ch);
		}

		if (!stream.isEof() && stream.get() == '.') {
			state |= DOT;
			sb.append(stream.get());
			while (stream.next() && Utilities.isDigit(ch = stream.get())) {
				state |= DEC;
				sb.append(ch);

			}
		}

		if (kinds[state] != null) {
			return new Token(sb.toString(), kinds[state], start, stream.position());
		}
		return null;
	}

	public static NumberMatcher createInteterMatcher (){
		return new NumberMatcher (){
			public Token match(CharStream stream){
				Token tk = super.match(stream);
				if(tk == null) return null;
				return tk.getKind().equals(TokenKind.INTEGER) ? tk : null;
			}
		};
	}
}
