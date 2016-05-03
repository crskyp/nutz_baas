package com.justep.baas.compiler.dotnet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.Utils;
import com.justep.baas.compiler.BaseCompiler;
import com.justep.baas.compiler.CompileException;
import com.justep.baas.model.Action;
import com.justep.baas.model.DBConfig;
import com.justep.baas.model.Service;

public class Compiler extends BaseCompiler{
	public void compile() {
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
	
	public Compiler(String compileLang, String modelDir, String outputDir, boolean outSource) {
		super(compileLang, modelDir, outputDir, outSource);
	}
	
	@Override
	protected DotNetCompileContext createContext(String modelDir, String outputDir, boolean outSource) {
		String outJar = getProperty("BAAS_MODEL_OUT_JAR");
		boolean isOutJar = null!=outJar && !"false".equalsIgnoreCase(outJar);
		String baasServerLibDir = getProperty("BAAS_LIB");
		if(isOutJar && Utils.isEmptyString(baasServerLibDir)){
			throw new CompileException("没有设置BAAS Server Lib 路径 BAAS_LIB");
		}
		String jarName = getProperty("BAAS_MODEL_OUT_JAR_FILENAME");
		if(isOutJar && Utils.isEmptyString(jarName)){
			throw new CompileException("没有设置BAAS模型编译输出JAR文件名BAAS_MODEL_OUT_JAR");
		}
		DotNetCompileContext context = new DotNetCompileContext(modelDir, outputDir, outSource, isOutJar, baasServerLibDir, jarName);		
		
		return context;
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
					if(name.endsWith(DotNetCompileContext.SERVICE_FILE_EXT)){
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
		final String outputFileExtensionName = ".cs";
		final String outputFileType = "C Sharp";
		String packageName = service.getPackageName();
		String className = service.getRunTimeClassName();
		String packageDir = mkPackageDir(packageName);
		String outputFileName = getContext().getOutputDir() + packageDir + className + outputFileExtensionName;
		File javaFile = new File(outputFileName);
		try {
			toFile(serviceCode,javaFile);
		} catch (IOException e) {
			throw new CompileException("创建" + outputFileType + "文件:"+outputFileName+"失败");			
		}
		
		//复制开发者java
		String sourceClassName = service.getClassName();
		File srcJava = new File(getContext().getModelDir() + packageDir + sourceClassName + outputFileExtensionName);
		if(srcJava.exists()){
			File targetJava = new File(getContext().getOutputDir() + packageDir + sourceClassName + outputFileExtensionName);
			try {
				copyFile(srcJava,targetJava);
			} catch (IOException e) {
				throw new CompileException("复制" + outputFileType + "文件:"+srcJava.getAbsolutePath()+"失败");
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
			
			String packageName = Utils.isEmptyString(service.getPackageName()) ? ((DotNetCompileContext)getContext()).PackageName : service.getPackageName();			
			String serviceCode = createServiceCode(packageName, service.getRunTimeClassName(), Str2ByteStr(dbCfg.toString()), sb.toString());
			createServiceFile(service, serviceCode);
		}
	}
}
