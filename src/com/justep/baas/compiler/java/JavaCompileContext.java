package com.justep.baas.compiler.java;

import com.justep.baas.compiler.CompileContext;
import com.justep.baas.compiler.Template;

public class JavaCompileContext extends CompileContext {
	public static final String TEMPLATE_FILE = "template.xml";
	boolean isOutJar = true;
	String BAASLib;
	String jarName;
	
	public JavaCompileContext(String modelDir, String outputDir, boolean outSource, boolean OutJar, String lib, String jarName) {
		super(modelDir, outputDir, outSource);
		isOutJar = OutJar;
		BAASLib = lib;
		this.jarName = jarName;
	}

	@Override
	public String PackagePrefix() {
		return "package ";
	}
	
	@Override
	public String PackageSuffix() {
		return ";";
	}
	
	@Override
	public Template createTemplate() {
		return new Template(this.getClass().getResourceAsStream(TEMPLATE_FILE), TEMPLATE_FILE, this);
	}

	public boolean isOutJar() {
		return isOutJar;
	}

	public String getBAASLib() {
		return BAASLib;
	}

	public String getJarName() {
		return jarName;
	}
}
