package com.jsptpd.weixin.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsptpd.weixin.thread.CheckOutKaoQingWeixinThread;

@SuppressWarnings("all")
public class CheckOutTimeSettingServlet  extends HttpServlet{

	@Override
	public void init() throws ServletException {
		  Runnable runnable=new CheckOutKaoQingWeixinThread();
		  
		  ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  
		  long oneDay = 24 * 60 * 60 * 1000;  
		  long initDelay  = getTimeMillis("17:50:00") - System.currentTimeMillis(); 
		  initDelay = initDelay > 0 ? initDelay : oneDay + initDelay; 
		  executor.scheduleAtFixedRate(runnable, initDelay,  
		            oneDay,  TimeUnit.MILLISECONDS);
	}
	
	
	private static long getTimeMillis(String time) {  
	    try {  
	        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");  
	        DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");  
	        Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);  
	        return curDate.getTime();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return 0;  
	}  
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}
	
	@Override
	public void destroy() {
		super.destroy();
		/*System.out.println(Thread.currentThread().getName());*/

	}
	

}
