package com.justep.baas.compiler.php;

import com.justep.baas.compiler.CompileContext;
import com.justep.baas.compiler.Template;

public class PHPCompileContext extends CompileContext {
	public static final String TEMPLATE_FILE = "template.xml";
	boolean isOutJar = true;
	String BAASLib;
	String jarName;
	public final String ExtensionName = ".php";
	public final String SourceCodeFileName = "PHP";
	public final String PackageName = "Com.Justep.Baas";
	
	public PHPCompileContext(String modelDir, String outputDir, boolean outSource, boolean OutJar, String lib, String jarName) {
		super(modelDir, outputDir, outSource);
		isOutJar = OutJar;
		BAASLib = lib;
		this.jarName = jarName;
	}

	@Override
	public Template createTemplate() {
		return new Template(this.getClass().getResourceAsStream(TEMPLATE_FILE), TEMPLATE_FILE, this);
	}

	public String getBAASLib() {
		return BAASLib;
	}

	public String getJarName() {
		return jarName;
	}
}
