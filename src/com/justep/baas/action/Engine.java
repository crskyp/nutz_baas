package com.justep.baas.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.Utils;

class ActionDef {
	String name;
	String clazz;

	ActionDef(String name, String clz) {
		this.name = name;
		this.clazz = clz;
	}
}

public class Engine {
	private static String getPackageName(String[] paths) {
		if (paths.length >= 2) {
			String name = "";
			for (int i = 0; i < paths.length - 2; i++) {
				name += (!"".equals(name) ? "." : "" + paths[i]);
			}
			return name;
		} else
			return null;
	}

	private static String getClassName(String[] paths) {
		if (paths.length >= 2) {
			String clz = paths[paths.length - 2];
			if (!Utils.isEmptyString(clz))
				clz = clz.substring(0, 1).toUpperCase() + clz.substring(1);
			return getRunTimeClassName(clz);
		} else
			return null;
	}

	private static String getActionName(String[] paths) {
		if (paths.length >= 2) {
			return paths[paths.length - 1];
		} else
			return null;
	}

	private static ActionDef getAction(String actionPath) {
		String[] paths = actionPath.split("/");
		if (paths.length >= 2) {
			String name = getActionName(paths);
			String clazzName = getClassName(paths);
			String packageName = getPackageName(paths);
			ActionDef action = new ActionDef(name, !Utils.isEmptyString(packageName) ? (packageName + "." + clazzName) : clazzName);
			return action;
		} else
			return null;
	}

	public static String getRunTimeClassName(String clazz){
		return clazz + "__do";
	}
	
	public static JSONObject execAction(String actionPath, JSONObject params) {
		return execAction(actionPath, params, null);
	}

	public static JSONObject execAction(String actionPath, JSONObject params, ActionContext context) {
		ActionDef action = getAction(actionPath);
		Class<?> ownerClass;
		Method method;
		try {
			ownerClass = Class.forName(action.clazz);
			method = ownerClass.getMethod(action.name, JSONObject.class, ActionContext.class);
			return (JSONObject) (method.invoke(null, params, context));
		} catch (ClassNotFoundException e) {
			throw new ActionException("Action["+actionPath+"] Class加载失败，可能原因：Baas模型没有编译，请Baas模型编译后重启服务！", e);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ActionException("Action["+actionPath+"] Method加载失败，可能原因：Baas模型没有编译，请Baas模型编译后重启服务！", e);
		} catch (InvocationTargetException e) {
			throw new ActionException("Action["+actionPath+"]执行失败，"+e.getTargetException().getMessage(), e);
		} catch (IllegalAccessException e){
			throw new ActionException("Action["+actionPath+"]执行失败，不能执行私有方法！", e);
		} catch (IllegalArgumentException e) {
			throw new ActionException("Action["+actionPath+"]执行失败，参数不匹配！", e);
		}
	}
	
}
