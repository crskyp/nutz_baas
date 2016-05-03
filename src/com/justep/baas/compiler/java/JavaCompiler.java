package com.justep.baas.compiler.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.tools.ToolProvider;

import com.justep.baas.Utils;
import com.justep.baas.compiler.CompileException;

public class JavaCompiler {
	
	private static String getClassPath(String libPath) {
		if(Utils.isEmptyString(libPath)) return "";
		String cutSign;
		String os = System.getProperty("os.name");
		if (os.contains("Windows")) {
			cutSign = ";";
		} else {
			cutSign = ":";
		}

		StringBuffer cp = new StringBuffer();
		File f = new File(libPath);
		File[] fs = f.listFiles();
		for (File file : fs) {
			if (file.getName().endsWith(".jar")) {
				cp.append(cutSign + file.getAbsolutePath());
			}
		}
		return cp.toString();
	}

	public static void compile(String sourcePath, String libPath, File from, File to) throws IOException {
		if (!to.exists())
			to.mkdirs();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		int result = compiler.run(null, null, out, "-encoding", "UTF-8", "-cp", getClassPath(libPath),"-sourcepath", sourcePath, "-d", to.getCanonicalPath(), from.getCanonicalPath());
		if (result != 0){
			throw new CompileException("编译模块Java出错 " +  
					System.getProperty("line.separator") + out.toString());
		}
	}
	
	public static void packJar(File from, File toFile){
		PackJar packJar = new PackJar(from, toFile);
		packJar.start();
	}
}
