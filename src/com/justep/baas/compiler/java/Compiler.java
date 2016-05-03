package com.justep.baas.compiler.java;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.Utils;
import com.justep.baas.compiler.BaseCompiler;
import com.justep.baas.compiler.CompileContext;
import com.justep.baas.compiler.CompileException;
import com.justep.baas.model.Action;
import com.justep.baas.model.DBConfig;
import com.justep.baas.model.Service;

public class Compiler extends BaseCompiler{
	public Compiler(String compileLang, String modelDir, String outputDir, boolean outSource) {
		super(compileLang, modelDir, outputDir, outSource);
	}

	@Override
	protected CompileContext createContext(String modelDir, String outputDir, boolean outSource) {
		String outJar = getProperty("BAAS_MODEL_OUT_JAR");
		//System.out.println("BAAS_MODEL_OUT_JAR:"+outJar);
		boolean isOutJar = null!=outJar && !"false".equalsIgnoreCase(outJar);
		String baasServerLibDir = getProperty("BAAS_LIB");
		//System.out.println("BAAS_LIB:"+baasServerLibDir);
		if(Utils.isEmptyString(baasServerLibDir)){
			throw new CompileException("没有设置BAAS Server Lib路径BAAS_LIB");
		}
		String jarName = getProperty("BAAS_MODEL_OUT_JAR_FILENAME");
		//System.out.println("BAAS_MODEL_OUT_JAR_FILENAME:"+jarName);
		if(isOutJar && Utils.isEmptyString(jarName)){
			throw new CompileException("没有设置BAAS模型编译输出JAR文件名BAAS_MODEL_OUT_JAR");
		}
		CompileContext context = new JavaCompileContext(modelDir, outputDir, outSource, isOutJar, baasServerLibDir, jarName);		
		
		return context;
	}

	private String mkPackageDir(String packageName){
		if(Utils.isEmptyString(packageName)) return "";
		String[] paths = packageName.split("\\.");
		String ret = "";
		for(int i=0; i<paths.length; i++){
			ret=(i==0?"":ret)+paths[i]+"/";
			File dir = new File(getContext().getOutputDir() + ret);
			if(!dir.exists()) dir.mkdirs();
		}
		return ret;
	}	

	private void createServiceFile(Service service,String serviceCode){
		String packageName = service.getPackageName();
		String className = service.getRunTimeClassName();
		String packageDir = mkPackageDir(packageName);
		String javaFileName = getContext().getOutputDir() + packageDir + className + ".java";
		File javaFile = new File(javaFileName);
		try {
			toFile(serviceCode,javaFile);
		} catch (IOException e) {
			throw new CompileException("创建java文件:"+javaFileName+"失败");			
		}
		//复制开发者java
		String sourceClassName = service.getClassName();
		File srcJava = new File(getContext().getModelDir() + packageDir + sourceClassName + ".java");
		if(srcJava.exists()){
			File targetJava = new File(getContext().getOutputDir() + packageDir + sourceClassName + ".java");
			try {
				copyFile(srcJava,targetJava);
			} catch (IOException e) {
				throw new CompileException("复制Java文件:"+srcJava.getAbsolutePath()+"失败");
			}
		}
	}

	private void compileFile(String file){
		Service service = Service.loadService(getContext(), file);
		
		List<Action> actions = service.getPublicActions();
		if(null!=actions && actions.size()>0){//没有action的service不生成代码
			StringBuffer sb = new StringBuffer();
			for(Action action : actions){
				sb.append(createActionCode(action));
			}
			
			DBConfig dbConfig = getContext().getDbConfig();
			JSONObject dbCfg = new JSONObject();
			for(String key : dbConfig.keySet()){
				dbCfg.put(key, dbConfig.get(key));
			}
			String serviceCode = createServiceCode(service.getPackageName(), service.getRunTimeClassName(), Str2ByteStr(dbCfg.toString()), sb.toString());
			createServiceFile(service, serviceCode);
		}
	}
	
	protected void compileDir(String sourceDir, String targetDir) throws IOException{
		File fSourceDir = new File(getContext().getModelDir()+sourceDir);
		if (!fSourceDir.exists())
			return;
		if (sourceDir.endsWith(".svn") || sourceDir.endsWith("out") || sourceDir.endsWith("bin") || sourceDir.endsWith("classes") || sourceDir.endsWith(".settings"))
			return;//.svn out bin classes不进行复制
		// 新建目标目录
		File fTargetDir = new File(getContext().getOutputDir()+targetDir);
		fTargetDir.mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = fSourceDir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				String name = sourceFile.getName();
				if(!name.endsWith(".classpath") && !name.endsWith(".project")){
					// 目标文件
					File targetFile = new File(fTargetDir.getAbsolutePath() + File.separator + name);
					copyFile(sourceFile, targetFile);
					if(name.endsWith(CompileContext.SERVICE_FILE_EXT)){
						compileFile(sourceDir + (!"".equals(sourceDir)?"/":"") + name);
					}
				}
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + (!"".equals(sourceDir)?"/":"") + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + (!"".equals(targetDir)?"/":"") + file[i].getName();
				compileDir(dir1, dir2);
			}
		}
	}
	
	private static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}
	
	private void compileJavaDir(String sourceDir, String targetDir, boolean outputSourceCode, String libDir) throws IOException{
		compileJavaDir(sourceDir, targetDir, null, outputSourceCode, libDir);
	}

	private void compileJavaDir(String sourceDir, String targetDir, String classesTargetDir, boolean outputSourceCode, String libDir) throws IOException{
		File fSourceDir = new File(sourceDir);
		if (!fSourceDir.exists())
			return;
		if (sourceDir.endsWith(".svn") || sourceDir.endsWith("out") || sourceDir.endsWith("bin") || sourceDir.endsWith("classes") || sourceDir.endsWith(".settings"))
			return;//.svn out bin classes不进行复制
		if(Utils.isEmptyString(classesTargetDir)) classesTargetDir = targetDir;
		// 新建目标目录
		File fTargetDir = new File(targetDir);
		fTargetDir.mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = fSourceDir.listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				String name = sourceFile.getName();
				if(!name.endsWith(".classpath") && !name.endsWith(".project")){
					// 目标文件
					if(name.endsWith(".java")){
						JavaCompiler.compile(getContext().getOutputDir(), libDir, sourceFile, new File(classesTargetDir));
					}
					if(outputSourceCode || (!outputSourceCode && !name.endsWith(".java") && !name.endsWith(CompileContext.SERVICE_FILE_EXT) && !CompileContext.DB_CONFIG_FILE.equalsIgnoreCase(name))){
						File targetFile = new File(fTargetDir.getAbsolutePath() + File.separator + name);
						copyFile(sourceFile, targetFile);
					}
				}
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + File.separator + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + File.separator + file[i].getName();
				compileJavaDir(dir1, dir2, classesTargetDir, outputSourceCode, libDir);
			}
		}
	}
	
	//编译整个baas模型目录
	public void compile2Java(){
		System.out.println("开始编译BAAS模型......");
		long startlong = System.currentTimeMillis();
		try {
			deleteFile(getContext().getOutputDir());
			compileDir("", "");

			long endlong = System.currentTimeMillis();
			System.out.println("编译BAAS模型结束");
			System.out.println("编译用时：  " + (endlong - startlong) / 1000 + " 秒");
		} catch (IOException e) {
			throw new CompileException("编译baas model["+getContext().getModelDir()+"]失败",e);
		}
	}

	//编译整个baas模型目录并生成jar
	public void compile2jar(File toFile, String libDir, boolean outputSourceCode){
		String classesDir = getTempDir()+"/baasClasses";
		compile2Java();
		compileJavaCode(classesDir,libDir,outputSourceCode);
		System.out.println("开始输出BAAS Service Jar......");
		long startlong = System.currentTimeMillis();
		JavaCompiler.packJar(new File(classesDir), toFile);
		long endlong = System.currentTimeMillis();
		System.out.println("编译BAAS Service Jar结束");
		System.out.println("打包用时：  " + (endlong - startlong) / 1000 + " 秒");
	}
	
	private void compileJavaCode(String classesDir, String libDir, boolean outputSourceCode){
		System.out.println("开始编译BAAS Service Java......");
		long startlong = System.currentTimeMillis();
		//清理输出目录
		deleteFile(classesDir);
		try {
			compileJavaDir(getContext().getOutputDir(),classesDir, outputSourceCode, libDir);
			long endlong = System.currentTimeMillis();
			System.out.println("编译BAAS Service Java结束");
			System.out.println("编译用时：  " + (endlong - startlong) / 1000 + " 秒");
		} catch (IOException e) {
			throw new CompileException("编译baas model["+getContext().getModelDir()+"] java 失败",e);
		}
	}
	
	public void compile2jar(String toFile, String libDir, boolean outputSourceCode){
		compile2jar(new File(toFile), libDir, outputSourceCode);
	}

	public void compile2jar(String toFile, String libDir){
		compile2jar(new File(toFile), libDir, false);
	}

	public void compile() {
		JavaCompileContext context = (JavaCompileContext)getContext();
		if(!context.isOutJar()){
			compile2Java();
			//增加java编译便于开发者得到出错提示
			String classesDir = getTempDir()+"/baasClasses";
			compileJavaCode(classesDir, context.getBAASLib(), context.isOutSource());
		}
		else{
			compile2jar(context.getJarName(), context.getBAASLib(), context.isOutSource());
		}
	}

}
