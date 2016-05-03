package com.justep.baas.model;

import com.justep.baas.BaasException;

public class ModelException extends BaasException {
	private static final long serialVersionUID = -5580009633031134086L;

	public ModelException(String msg){
		super(msg);
	}

	public ModelException(String msg, Exception exception){
		super(msg, exception);
	}

}
