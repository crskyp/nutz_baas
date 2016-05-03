package com.justep.baas.model;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import com.justep.baas.Utils;
import com.justep.baas.data.DataType;
import com.justep.baas.data.Transform;

public class Action {
	private boolean isImplByAction;
	protected Element def;
	protected String name;
	protected String impl;
	protected Service service;
	protected boolean _private = false;
	protected Map<String,ActionParam> privateParams = new HashMap<String,ActionParam>();
	protected Map<String,ActionParam> publicParams = new HashMap<String,ActionParam>();

	public Action(Service service, Element ele){
		this.service = service;
		def = ele;
		load();
	}
	
	protected void load(){
		//先加载自己的参数定义
		loadParams("private");
		loadParams("public");
		
		name = def.attributeValue("name");
		_private = "true".equalsIgnoreCase(def.attributeValue("private"));
		String sImpl = def.attributeValue("impl");
		isImplByAction = sImpl.startsWith("action:");
		if(!isImplByAction) impl = sImpl;
	}
	
	protected void processImplAction(){
		//延后处理，解决循环依赖问题
		if(isImplByAction){
			String sImpl = def.attributeValue("impl");
			String actionPath = sImpl.substring("action:".length());
			Action action = Service.getAction(service.context, actionPath);
			if(null!=action){
				impl = action.getImpl();
				Map<String,ActionParam> params = action.getPrivateParams();
				for(String key : params.keySet()){
					if(!privateParams.containsKey(key)){
						privateParams.put(key, params.get(key));
					}
				}
				params = action.getPublicParams();
				for(String key : params.keySet()){
					if(!publicParams.containsKey(key)){
						publicParams.put(key, params.get(key));
					}
				}
			}else throw new ModelException("加载action["+actionPath+"]失败");
			isImplByAction = false;
		}
	}
	
	protected Object toValue(String str, String type){
		if(Utils.isEmptyString(str)) return null;
		DataType dt = DataType.parse(type);
		try {
			return Transform.strToValue(str, dt);
		} catch (ParseException e) {
			throw new ModelException("值转换失败，value:"+str+"，type:"+type,e);
		}
	}

	protected void loadParams(String type){
		boolean isPrivate = "private".equalsIgnoreCase(type);
		if(isPrivate)privateParams.clear(); 
		else publicParams.clear();
		Iterator<?> it = def.elementIterator(isPrivate?"private":"public");
		while(it.hasNext()){
			Element param = (Element)(it.next());
			String datatype = param.attributeValue("type");
			Object value = toValue(param.getTextTrim(),datatype);			
			ActionParam ap = new ActionParam(param.attributeValue("name"),datatype,value);
			if(isPrivate)privateParams.put(ap.name, ap); 
			else publicParams.put(ap.name, ap);
		}
	}
	
	public String getName(){
		return name;
	}

	public String getImpl(){
		processImplAction();
		return impl;
	}
	
	public Map<String,ActionParam> getPrivateParams(){
		processImplAction();
		return privateParams;
	}

	public Map<String,ActionParam> getPublicParams(){
		processImplAction();
		return publicParams;
	}

	public boolean isPrivate() {
		return _private;
	}
}
