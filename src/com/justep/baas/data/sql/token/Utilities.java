package com.justep.baas.data.sql.token;

import com.justep.baas.data.sql.token.CharStream;

public class Utilities {
	public static boolean isDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	public static boolean isLetter(char ch) {

		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
	}

	public static boolean isWhite(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
	}

	public static void skipWhite(CharStream stream) {
		if (stream.isEof())
			return;
		for (char ch = stream.get(); isWhite(ch) && stream.next(); ch = stream.get())
			;
	}

	public static boolean isSign(char ch) {
		return ch == '+' || ch == '-';
	}

}
