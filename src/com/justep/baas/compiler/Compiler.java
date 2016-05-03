package com.justep.baas.compiler;

public interface Compiler {
	public CompileContext getContext();
	public void compile();
	public String getCompileLang();
}
