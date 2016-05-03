package com.justep.baas.compiler;

import com.justep.baas.model.ModelContext;

public abstract class CompileContext extends ModelContext{
	private String outputDir;
	private Template template;
	private boolean outSource = true;
	
	public CompileContext(String modelDir, String outputDir, boolean outSource){
		super(modelDir);
		this.outputDir = outputDir;
		this.outSource = outSource;
		
		this.template = createTemplate();
	}
	
	abstract public Template createTemplate();
	
	public String PackagePrefix() {
		return "";
	}
	
	public String PackageSuffix() {
		return "";
	}
	
	public String getOutputDir() {
		return outputDir;
	}

	public Template getTemplate() {
		return template;
	}

	public boolean isOutSource() {
		return outSource;
	}

}
