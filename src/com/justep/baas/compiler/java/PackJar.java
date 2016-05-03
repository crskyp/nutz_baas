package com.justep.baas.compiler.java;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import com.justep.baas.compiler.CompileException;

public class PackJar {
	static final int BUFFER = 8 * 1024;
	private String filePath;
	private JarOutputStream out;
	private File fromFile;
	private File toFile;

	public PackJar(File fromFile, File toFile) {
		filePath = fromFile.getAbsolutePath() + "\\";
		this.fromFile = fromFile;
		this.toFile = toFile;
	}

	public void start() {
		try {
			FileOutputStream fos = new FileOutputStream(toFile);
			out = new JarOutputStream(new BufferedOutputStream(fos));
			try {
				makeJar(fromFile);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new CompileException("创建jar[" + toFile.getAbsolutePath() + "]失败", e);
		}
	}

	private void makeJar(File fromFile) throws IOException {
		File[] fileList = fromFile.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File _f = fileList[i];
			if (_f.isDirectory()) {
				makeJar(_f);
			} else {
				String path = _f.getAbsolutePath();
				path = getAbsolutePath(path);
				writeEntry(path, _f, out);
			}
		}
	}

	private void writeEntry(String EntryName, File file, JarOutputStream out) throws IOException {
		JarEntry entry = new JarEntry(EntryName);
		out.putNextEntry(entry);
		FileInputStream fi = new FileInputStream(file);
		BufferedInputStream origin = null;
		origin = new BufferedInputStream(fi, BUFFER);

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = origin.read(data, 0, BUFFER)) != -1) {
			out.write(data, 0, count);
		}
		origin.close();

	}

	/**
	 * 重写文件路径，主要是为了统一格式.
	 * @param oldFilePath 旧的路径
	 * @return
	 */
	static String rewriteFilePath(String oldFilePath) {
		return oldFilePath.replace("\\", "/").replace("\\\\", "/");
	}
	
	private String getAbsolutePath(String strPath) {
		String ret = strPath.substring(filePath.length(), strPath.length());
		return rewriteFilePath(ret);
	}
}