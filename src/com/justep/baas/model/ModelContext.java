package com.justep.baas.model;

import java.util.HashMap;
import java.util.Map;

import com.justep.baas.model.DBConfig;

public class ModelContext {
	public static final String SERVICE_FILE_EXT = ".service.m";	
	public static final String DB_CONFIG_FILE = "db.config.m";	
	
	private String modelDir;
	private DBConfig dbConfig;
	
	private Map<String, Object> values = new HashMap<String, Object>();
	
	public ModelContext(String modelDir){
		this.modelDir = modelDir;
		
		this.dbConfig = new DBConfig(modelDir+DB_CONFIG_FILE, this);
	}
	
	public Object get(String key){
		return values.get(key);
	}
	
	public void put(String key, Object value){
		values.put(key, value);
	}
	
	public void clear(){
		values.clear();
	}

	public boolean containsKey(String key){
		return values.containsKey(key);
	}

	public String getModelDir() {
		return modelDir;
	}

	public DBConfig getDbConfig() {
		return dbConfig;
	}
	
}
