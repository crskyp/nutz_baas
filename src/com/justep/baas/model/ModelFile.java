package com.justep.baas.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ModelFile {
	protected String file;
	protected Document doc;
	protected ModelContext context;
	
	public ModelFile(String file, ModelContext context){
		this.file = file;
		this.context = context;
		load();
	}
	
	public ModelFile(InputStream fileIS, String file, ModelContext context){
		this.context = context;
		this.file = file;
		load(fileIS);
	}
	
	public void load(InputStream fileIS){
		try {
			SAXReader reader = new SAXReader();
			doc = reader.read(fileIS);
		} catch (Exception e) {
			throw new ModelException("加载文件["+file+"]失败",e);
		}		
	}

	public void load(){
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File(file));
		} catch (FileNotFoundException e) {
			throw new ModelException("加载文件["+file+"]失败",e);
		}
		this.load(fis);
	}

	public Document getDocument(){
		return doc;
	}
	
	public Element getRootElement(){
		return doc.getRootElement();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

	public String getFile() {
		return file;
	}

}
