package com.justep.baas.compiler;

import java.io.InputStream;

import org.dom4j.Element;

import com.justep.baas.model.ModelFile;

public class Template extends ModelFile {
	protected String serviceTemplate;
	protected String actionTemplate;

	public Template(String file, CompileContext context) {
		super(file, context);
	}

	public Template(InputStream is, String name, CompileContext context) {
		super(is, name, context);
	}

	public void load(InputStream fileIS) {
		super.load(fileIS);

		Element root = getRootElement();
		serviceTemplate = root.elementText("service");
		actionTemplate = root.elementText("action");
	}

	public String getServiceTemplate() {
		return serviceTemplate;
	}

	public String getActionTemplate() {
		return actionTemplate;
	}

	public String createServiceCode(String packageName,String reference, String className, String dbConfig, String actionsCode) {
		String serviceCode = serviceTemplate;
		String temp = serviceCode.replaceAll("%__packageName__%", packageName).replaceAll("%__className__%", className)
				.replaceAll("%__DBConfig__%", dbConfig).replaceAll("%__reference__%", reference);
		return ReplaceString(temp, "%__actions__%", actionsCode);
	}
	
	public String createServiceCode(String packageName, String className, String dbConfig, String actionsCode) {
		String serviceCode = serviceTemplate;
		String temp = serviceCode.replaceAll("%__packageName__%", packageName).replaceAll("%__className__%", className)
				.replaceAll("%__DBConfig__%", dbConfig);
		return ReplaceString(temp, "%__actions__%", actionsCode);
	}
	
	public String ReplaceString(String original, String tagStr, String newStr)
	{
		int tagIndex = original.indexOf(tagStr);
		String temp = original.substring(0, tagIndex) + newStr + original.substring(tagIndex + tagStr.length());		
		return temp;
	}

	public String createActionCode(String actionName, String actionImplName, String privateParamsCode, String publicParamsCode) {
		String actionCode = actionTemplate;
		String tempStr = actionCode.replaceAll("%__actionName__%", actionName);
		//.replaceAll("%__implActionName__%", actionImplName)
		tempStr = ReplaceString(tempStr, "%__implActionName__%", actionImplName);
		return tempStr.replaceAll("%__privateParams__%", privateParamsCode).replaceAll("%__publicParams__%", publicParamsCode);
	}

}
