package com.justep.baas.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.justep.baas.Utils;
import com.justep.baas.action.Engine;

public class Service extends ModelFile {
	protected List<Action> actions;
	protected String className;
	protected String runTimeClassName;
	protected String packageName;

	private static String getPackageName(String[] paths) {
		if (paths.length >= 1) {
			String name = "";
			for (int i = 0; i < paths.length - 1; i++) {
				name += ((!"".equals(name) ? "." : "") + paths[i]);
			}
			return name;
		} else
			return null;
	}

	private static String getClassName(String[] paths) {
		if (paths.length >= 1) {
			String clazz = paths[paths.length - 1];
			if (!Utils.isEmptyString(clazz))
				clazz = clazz.substring(0, 1).toUpperCase() + clazz.substring(1);
			return clazz;
		} else
			return null;
	}
	
	public static Action getAction(ModelContext context, String actionPath){
		int pos = actionPath.lastIndexOf("/");
		if(pos>0){
			String serviceFile = actionPath.substring(0, pos) + ModelContext.SERVICE_FILE_EXT;
			String actionName = actionPath.substring(pos+1);
			Service service = loadService(context, serviceFile);
			return service.getAction(actionName);
		}return null;
	}
	
	public static Service loadService(ModelContext context, String file){
		@SuppressWarnings("unchecked")
		Map<String, Service> services = (Map<String, Service>)(context.get("sys.service"));
		
		if(null==services){
			services = new HashMap<String, Service>();
			context.put("sys.service",services);
		}
		
		//一次编译过程中进行缓存处理
		if(services.containsKey(file)) return services.get(file);
		else{
			String packageName = null;
			String className = null;
			String ext = ModelContext.SERVICE_FILE_EXT;
			String temp = file;
			if(file.endsWith(ext)) temp = temp.substring(0, temp.lastIndexOf(ext));
			String[] paths = temp.split("/");
			if (paths.length >= 1) {
				packageName = getPackageName(paths);
				className = getClassName(paths);
			}else throw new ModelException("无效的model文件:"+file);		
			
			Service service = new Service(context.getModelDir()+file, context, packageName, className);
			services.put(file, service);
			return service;
		}
	}
	
	public Service(String file, ModelContext context, String packageName, String className) {
		super(file, context);
		this.className = className;
		this.runTimeClassName = Engine.getRunTimeClassName(className);
		this.packageName = packageName;
	}

	public void load(){
		super.load();
		
		actions = new ArrayList<Action>();
		Element root = getRootElement();
		Iterator<?> it = root.elementIterator("action");
		while(it.hasNext()){
			Element action = (Element)(it.next());
			actions.add(new Action(this, action));
		}
	}

	public Action getAction(String name){
		if(null!=name && !"".equals(name)){
			for(Action act : actions){
				if(name.equals(act.getName())) return act;
			}
		}
		return null;
	}
	
	public List<Action> getActions() {
		return actions;
	}

	public List<Action> getPublicActions() {
		List<Action> ret = new ArrayList<Action>();
		for(Action action : actions){
			if(!action.isPrivate()) ret.add(action);
		}
		return ret;
	}
	
	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getRunTimeClassName() {
		return runTimeClassName;
	}	
}
