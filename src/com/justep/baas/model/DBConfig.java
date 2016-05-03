package com.justep.baas.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

public class DBConfig extends ModelFile {
	protected Map<String,String> datasource;
	
	public DBConfig(String file, ModelContext context) {
		super(file, context);
	}

	public void load(){
		super.load();
		
		datasource = new HashMap<String,String>();
		Element root = getRootElement();
		Iterator<?> it = root.elementIterator("datasource");
		while(it.hasNext()){
			Element db = (Element)(it.next());
			datasource.put(db.attributeValue("name"), db.attributeValue("value"));
		}
	}
	
	public String get(String key){
		return datasource.get(key);
	}

	public boolean containsKey(String key){
		return datasource.containsKey(key);
	}
	
	public Set<String> keySet(){
		return datasource.keySet();
	}
}
