package com.justep.baas.compiler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.justep.baas.Utils;
import com.justep.baas.model.Action;
import com.justep.baas.model.ActionParam;

public abstract class BaseCompiler implements Compiler {
	private CompileContext context;
	private String compileLang;

	public static List<String> CustomActionList = new ArrayList<String>();

	public static void deleteFile(String filepath) {
		File f = new File(filepath);// 定义文件路径
		if (f.exists()) {// 判断是文件还是目录
			if (f.isDirectory()) {
				if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
					f.delete();
				} else {// 若有则把文件放进数组，并判断是否有下级目录
					File delFile[] = f.listFiles();
					int len = delFile.length;
					for (int j = 0; j < len; j++) {
						if (delFile[j].isDirectory()) {
							deleteFile(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
						}
						delFile[j].delete();// 删除文件
					}
				}
			} else
				f.delete();
		}
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 8];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	protected static void toFile(String code, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new ByteArrayInputStream(code.getBytes("UTF-8")));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 4];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	public BaseCompiler(String compileLang, String modelDir, String outputDir, boolean outSource) {
		this.compileLang = compileLang;
		context = createContext(modelDir, outputDir, outSource);
	}

	abstract protected CompileContext createContext(String modelDir, String outputDir, boolean outSource);

	@Override
	abstract public void compile();

	@Override
	public CompileContext getContext() {
		return context;
	}

	@Override
	public String getCompileLang() {
		return compileLang;
	}

	protected String Str2ByteStr(String str) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			byte[] bytes = str.getBytes("UTF-8");
			for (int i = 0; i < bytes.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(bytes[i]);
			}
			sb.append("}");
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			throw new CompileException("字符串-->字节数组失败", e);
		}
	}

	protected String Str2ByteStrForPHP(String str) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			byte[] bytes = str.getBytes("UTF-8");
			for (int i = 0; i < bytes.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(bytes[i]);
			}
			sb.append(")");
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			throw new CompileException("字符串-->字节数组失败", e);
		}
	}

	protected String getProperty(String key) {
		return System.getenv().get(key);
	}

	protected String createActionCode(Action action) {
		String actionName = action.getName();
		String actionImplName = action.getImpl();
		JSONObject privateParamsCode = new JSONObject();
		JSONObject publicParamsCode = new JSONObject();
		Map<String, ActionParam> aps = action.getPrivateParams();
		for (String key : aps.keySet()) {
			ActionParam ap = aps.get(key);
			privateParamsCode.put(key, ap.value);
		}
		aps = action.getPublicParams();
		for (String key : aps.keySet()) {
			ActionParam ap = aps.get(key);
			publicParamsCode.put(key, ap.value);
		}
		return context.getTemplate().createActionCode(actionName, actionImplName, Str2ByteStr(privateParamsCode.toString()), Str2ByteStr(publicParamsCode.toString()));
	}

	protected String createActionCodeForPHP(Action action) {
		String actionName = action.getName();
		String actionImplName = action.getImpl();

		if (actionImplName.indexOf("::") > -1) {
			//System.out.println(actionImplName);
			String realPhpFile = actionImplName.substring(0, actionImplName.indexOf("::"));
			if (!realPhpFile.equals("com\\justep\\baas\\action\\CRUD")) {
				if (!CustomActionList.contains(realPhpFile)) {
					CustomActionList.add(realPhpFile);
					//System.out.println(realPhpFile);
				}
			}
		}

		JSONObject privateParamsCode = new JSONObject();
		JSONObject publicParamsCode = new JSONObject();
		Map<String, ActionParam> aps = action.getPrivateParams();
		for (String key : aps.keySet()) {
			ActionParam ap = aps.get(key);
			privateParamsCode.put(key, ap.value);
		}
		aps = action.getPublicParams();
		for (String key : aps.keySet()) {
			ActionParam ap = aps.get(key);
			publicParamsCode.put(key, ap.value);
		}
		return context.getTemplate().createActionCode(actionName, actionImplName, Str2ByteStrForPHP(privateParamsCode.toString()), Str2ByteStrForPHP(publicParamsCode.toString()));
	}

	protected String createServiceCode(String packageName, String className, String dbConfig, String actionCodes) {
		if (Utils.isEmptyString(packageName))
			packageName = "";
		else {
			packageName = context.PackagePrefix() + packageName + context.PackageSuffix();
		}
		return context.getTemplate().createServiceCode(packageName, className, dbConfig, actionCodes);
	}

	protected String createServiceCodeForPHP(String packageName, String reference, String className, String dbConfig, String actionCodes) {
		if (Utils.isEmptyString(packageName))
			packageName = "";
		else {
			packageName = context.PackagePrefix() + packageName + context.PackageSuffix();
		}
		return context.getTemplate().createServiceCode(packageName, reference, className, dbConfig, actionCodes);
	}
}
