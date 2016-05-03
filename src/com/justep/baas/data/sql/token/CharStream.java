package com.justep.baas.data.sql.token;

import java.util.Stack;

import com.justep.baas.data.sql.token.ExpEOFException;
import com.justep.baas.data.sql.token.Markable;
import com.justep.baas.data.sql.token.Utilities;

public class CharStream implements Markable {
	private char[] buffer = null;
	private Stack<Integer> mark = new Stack<Integer>();
	private int pos = 0;

	public CharStream(char[] buff) {
		buffer = buff;
	}

	public CharStream(String s) {
		this(s.toCharArray());
	}

	public void back() {
		pos = mark.pop();
	}

	public void mark() {
		mark.push(pos);
	}

	public void unmark() {
		mark.pop();
	}

	//	private boolean hasNext(){
	//		return pos < buffer.length -1;
	//	}

	public boolean isEof() {
		return pos >= buffer.length;
	}

	public boolean next() {
		if (!isEof())
			pos++;
		return !isEof();

	}

	public char get() {
		if (isEof())
			throw new ExpEOFException();
		return buffer[pos];
	}

	public int position() {
		return pos;
	}

	public int length() {
		return buffer.length;
	}

	public String subString(int start, int end) {
		if (start < 0)
			start = 0;
		if (start > buffer.length)
			start = buffer.length;
		return new String(buffer, start, end - start);
	}

	//	public void backward() {
	//		pos--;
	//		
	//	}
	//	public void forward() {
	//		pos++;
	//		
	//	}

	private String lastWord = null;
	private int lastStart = -1, lastEnd = -1;

	public String nextWord() {
		if (isEof())
			return null;
		if (lastStart == position()) {
			pos = lastEnd;
			return lastWord;
		}
		int start = pos;
		char ch = get();
		if (!Utilities.isLetter(ch) && ch != '_')
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append(ch);
		while (next()) {
			ch = get();
			if (ch == '_' || Utilities.isLetter(ch) || Utilities.isDigit(ch))
				sb.append(ch);
			else
				break;
		}
		lastStart = start;
		lastEnd = pos;
		lastWord = sb.toString();
		return lastWord;

	}

	public char skipWhite() {
		if (isEof())
			return 0;
		while (Utilities.isWhite(get()) && next())
			;
		return isEof() ? 0 : get();

	}

}
