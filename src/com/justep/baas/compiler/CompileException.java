package com.justep.baas.compiler;

import com.justep.baas.model.ModelException;

public class CompileException extends ModelException {
	private static final long serialVersionUID = 980371655323504508L;

	public CompileException(String msg){
		super(msg);
	}

	public CompileException(String msg, Exception exception){
		super(msg, exception);
	}

}
