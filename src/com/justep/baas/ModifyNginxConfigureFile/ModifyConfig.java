package com.justep.baas.ModifyNginxConfigureFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ModifyConfig {
	
	private static String OS = System.getProperty("os.name").toLowerCase(); 
	private static String Separator;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Separator = Separator();
				
		ModifyNginxConfig();
	}

	private static void ModifyNginxConfig() throws IOException {
		String tmpStr = System.getProperty("user.dir");
		//tmpStr = "E:\\zjy\\work\\WeX5_4877\\tools\\compile";
		//tmpStr = "/Volumes/X5/WeX5_4898-mac";
		//System.out.println(tmpStr);
		
		String currentPath = tmpStr;
		if (tmpStr.indexOf("tools" + Separator + "compile") != -1)
		{
			currentPath = tmpStr.substring(0, tmpStr.indexOf("tools" + Separator + "compile"));
		}
		
		if (!currentPath.substring(currentPath.length() - 1).equals(Separator)) {
			currentPath = currentPath + Separator;
		}
		
		List<String> paths = new ArrayList<String>();
		paths.add(currentPath);
		paths.add(currentPath + "model" + Separator + "Baas.php" + Separator);
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
		
		String phpRoot = currentPath + "model" + Separator + "Baas.php" + Separator;
		String textContent = ReadTextFromFile(currentPath+"nginx" + Separator + "conf" + Separator + "nginx.conf");
		WriteTextToFile(
				currentPath+"nginx" + Separator + "conf" + Separator + "nginx.conf", 
				textContent.replace("C:/wamp/www/", phpRoot.replace("\\", "/"))
				);
		String path = currentPath+"nginx" + Separator + "conf" + Separator + "nginx.conf";
		
		ReplaceText(path, "C:/wamp/www/", phpRoot.replace("\\", "/"));
	}
	
	/**
     * 将文件中指定内容的第一行替换为其它内容.
     * 
     * @param oldStr
     *            查找内容
     * @param replaceStr
     *            替换内容
     */
    public static void ReplaceText(String path, String oldStr,String replaceStr) {
        String temp = "";
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // 保存该行前面的内容
            // && !temp.equals(oldStr)
            for (; (temp = br.readLine()) != null;) {
                buf = buf.append(temp.replace(oldStr, replaceStr));
                buf = buf.append(System.getProperty("line.separator"));
            }

            br.close();
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文本文件.
     * 
     */
    public static String ReadTextFromFile(String filename){
        String read;
        FileReader fileread = null;
        String readStr = "";
        try {
            fileread = new FileReader(filename);
            BufferedReader bufread = new BufferedReader(fileread);
            try {
                while ((read = bufread.readLine()) != null) {
                    readStr = readStr + read+ System.getProperty("line.separator");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally{
        	try {
        		if(null!=fileread)	fileread.close();
        	} catch (IOException e) {
                e.printStackTrace();
        	}
        }

        //System.out.println("文件内容是:"+ "\r\n" + readStr);
        return readStr;
    }
    
    /**
     * 写文件.
     * 
     */
    public static void WriteTextToFile(String filename, String newStr) throws IOException{
        //先读取原有文件内容，然后进行写入操作
        String filein = newStr;
        RandomAccessFile mm = null;
        try {
            mm = new RandomAccessFile(filename, "rw");
            mm.writeBytes(filein);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (mm != null) {
                try {
                    mm.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    public static String Separator()
    {
    	
    	//System.out.println(OS);
    	// windows
    	if (OS.indexOf("windows")>=0)
    	{
    		return "\\";
    	}
    	
    	// Linux
    	if (OS.indexOf("linux")>=0)
    	{
    		return "/";
    	}
    	
    	// Mac
    	if (OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")<0)
    	{
    		return "/";
    	}
    	
    	
    	return "/";
    }
}

	
