package com.justep.baas.data.sql.token;

import com.justep.baas.data.sql.token.Token;
import com.justep.baas.data.sql.token.TokenKind;

public class Token {
	private String image = null;
	private TokenKind kind = null;
	private int start = 0; //起始位置
	private int end = 0;   //结束位置
	
	public Token next = null;
	
	public Token(String image, TokenKind kind){
		this.image = image;
		this.kind = kind;
	}
	
	public Token (String image, TokenKind kind, int start, int end){
		this(image, kind);
		this.start = start;
		this.end = end;
	}
	
	public TokenKind getKind (){
		return this.kind;
	}
	
	public String getImage(){
		return image;
	}
	
	public void setStart(int start){
		this.start = start;
	}
	public int getStart(){
		return start;
	}
	
	public void setEnd(int end){
		this.end = end;
	}
	public int getEnd(){
		return end;
	}
	@Override
	public String toString(){
		return image;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Token)) return false;
		Token tk = (Token)obj;
		return this.kind.equals(tk.kind) && this.image.equals(tk.image) && this.start == tk.start && this.end == tk.end;
	}
	
	
	
}
