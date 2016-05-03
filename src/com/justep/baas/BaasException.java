package com.justep.baas;

public class BaasException extends RuntimeException {
	private static final long serialVersionUID = 4788254265308389054L;

	public BaasException(String msg){
		super(msg);
	}

	public BaasException(String msg, Exception exception){
		super(msg, exception);
	}
}
