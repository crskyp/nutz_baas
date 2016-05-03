package com.justep.baas.BuildForPHP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Builder {

	static String rootPath;
	static List<String> currentFileList = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		String tmpStr = System.getProperty("user.dir");
		//tmpStr = "E:\\zjy\\work\\WeX5_4877\\tools\\compile";
		String currentPath = tmpStr.substring(0, tmpStr.indexOf("tools\\compile"));
		if (currentPath.substring(currentPath.length() - 1) != "\\") {
			currentPath = currentPath + "\\";
		}

		List<String> paths = new ArrayList<String>();
		paths.add(currentPath);
		paths.add(currentPath + "model\\Baas.php\\");
		paths.add(currentPath + "nginx");
		paths.add(currentPath + "php");

		for (String item : paths) {
			// System.out.println(item);
			File file = new File(item);
			if (!file.exists() && !file.isDirectory()) {
				System.out.println(item + " is invalid.");
				return;
			}
		}

		// System.out.println(currentPath);
		// System.out.println(System.getProperty("user.dir"));
		 // "E:\\zjy\\work\\WeX5_4877\\tools\\compile";
		rootPath = currentPath + "model\\Baas.php\\";
		//System.out.println(rootPath);
		File root = new File(rootPath);
		showAllFiles(root);

		List<String> systemFiles = new ArrayList<String>();
		systemFiles.add("ActionContext.class.php");
		systemFiles.add("ActionDef.class.php");
		systemFiles.add("BaasData.php");
		systemFiles.add("class.phplock.php");
		systemFiles.add("CRUD.class.php");
		systemFiles.add("DBConfig.class.php");
		systemFiles.add("DemoService.php");
		systemFiles.add("Engine.class.php");
		systemFiles.add("index.php");
		systemFiles.add("TakeoutService.php");
		systemFiles.add("X5BaasService.php");
		systemFiles.add("reference.php");

		for (String item : systemFiles) {
			if (currentFileList.contains(item)) {
				currentFileList.remove(item);
			}
		}

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(rootPath + "reference.php"));
			out.write("<?php \r\n");
			for (String item : currentFileList) {
				//System.out.println(item);
				String phpFile = item.replace("\\", "/");
				//System.out.println("require_once(dirname(__FILE__).'/" + phpFile + "');");
				out.write("    require_once(dirname(__FILE__).'/" + phpFile + "');\r\n");
			}
			
			out.write("?>");
			out.close();
		} catch (IOException e) {
		}

		System.out.println("Generate reference php file completely.");
	}

	private static void showAllFiles(File dir) throws Exception {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			//System.out.println(fs[i].getAbsolutePath());
			if (fs[i].isFile()) {
				String fileName = fs[i].getName();
				if ("__do.php".equals(fileName.substring(fileName.length() - 8))){
					String fullPath = fs[i].getAbsolutePath();
					//System.out.println(fullPath);
					String relativePath = fullPath.substring(rootPath.length()-1);
					currentFileList.add(relativePath);
					// System.out.println(relativePath);
					// System.out.println(fs[i].getAbsolutePath());
					// System.out.println(fileExtensionName);
				}
			} else if (fs[i].isDirectory()) {
				try {
					showAllFiles(fs[i]);
				} catch (Exception e) {
				}
			}
		}
	}

	/*
	private static String getExtensionName(File fs) {
		String fileName = fs.getName();
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return prefix;
	}
	*/
}
