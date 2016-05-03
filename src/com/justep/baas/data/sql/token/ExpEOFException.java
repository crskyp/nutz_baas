package com.justep.baas.data.sql.token;

public class ExpEOFException extends RuntimeException {

	private static final long serialVersionUID = 2131522077347431384L;

	public ExpEOFException() {
		
	}

	public ExpEOFException(String message) {
		super(message);
	}

	public ExpEOFException(Throwable cause) {
		super(cause);
	}

	public ExpEOFException(String message, Throwable cause) {
		super(message, cause);
	}

}
