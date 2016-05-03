package com.justep.baas.model;

public class ActionParam {
	public String name;
	public String type;
	public Object value;
	
	public ActionParam(String name,	String type, Object value){
		this.name = name;
		this.type = type;
		this.value = value;
	}
}
