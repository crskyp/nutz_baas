package com.justep.baas.data.sql;

import com.justep.baas.BaasException;

public class SQLException extends BaasException {
	private static final long serialVersionUID = -6774208105258505926L;

	public SQLException(String msg){
		super(msg);
	}

	public SQLException(String msg, Exception exception){
		super(msg, exception);
	}

}
