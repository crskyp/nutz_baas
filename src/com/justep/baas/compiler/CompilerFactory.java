package com.justep.baas.compiler;

import com.justep.baas.Utils;

public class CompilerFactory {
	public static final String JAVA = "JAVA";
	public static final String PHP = "PHP";
	public static final String DOT_NET = ".net";
	
	public static Compiler createCompiler(String lang, String modelDir, String outputDir, boolean isOutSource){
		if(JAVA.equalsIgnoreCase(lang)) return new com.justep.baas.compiler.java.Compiler(lang, modelDir, outputDir, isOutSource);
		if(DOT_NET.equalsIgnoreCase(lang)) return new com.justep.baas.compiler.dotnet.Compiler(lang, modelDir, outputDir, isOutSource);
		if(PHP.equalsIgnoreCase(lang)) return new com.justep.baas.compiler.php.Compiler(lang, modelDir, outputDir, isOutSource);
		else throw new CompileException("不支持的语言："+lang);
	}
	
	private static String getProperty(String key){
		String name = System.getProperty(key);
		if(name != null){
			return name;
		}
		return System.getenv().get(key);
	}
	
	public static void main(String[] args) {
		try {
			//java X:\X5\model\BAAS\justep  X:\X5\model\BAASServer\baas\justep
			//    "X:/X5/model/BAAS/",     "X:/X5/runtime/BAASServer/baas/" "X:/BAAS_service.jar" "X:/X5/runtime/BAASServer/WEB-INF/lib" true true
			String lang = getProperty("COMPILE_LANG");
			if(Utils.isEmptyString(lang)) lang = args[0];
			if(Utils.isEmptyString(lang)){
				System.out.println("没有设置BAAS模型编译语言COMPILE_LANG");
				return;
			}
			String baasModelDir = getProperty("BAAS_MODEL_PATH");
			if(Utils.isEmptyString(baasModelDir)) baasModelDir = args[1];
			if(Utils.isEmptyString(baasModelDir)){
				System.out.println("没有设置BAAS模型路径BAAS_MODEL_PATH");
				return;
			}
			if(!baasModelDir.endsWith("\\") && !baasModelDir.endsWith("/")) baasModelDir += "/";
			String outDir = getProperty("BAAS_MODEL_OUT_DIR");
			if(Utils.isEmptyString(outDir)) outDir = args[2];
			if(Utils.isEmptyString(outDir)){
				System.out.println("没有设置BAAS模型编译输出目录BAAS_MODEL_OUT_DIR");
				return;
			}
			if(!outDir.endsWith("\\") && !outDir.endsWith("/")) outDir += "/";
			String outSource = getProperty("BAAS_MODEL_OUT_SOURCE");
			boolean isOutSource = null!=outSource && !"false".equalsIgnoreCase(outSource);
			Compiler compiler = createCompiler(lang, baasModelDir, outDir, isOutSource);
			compiler.compile();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}	
}
