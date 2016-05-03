package com.jsptpd.weixin.thread;

import com.jsptpd.weixin.call.action.X5Actions;
import com.justep.biz.client.Action;
import com.justep.biz.client.ActionResult;

public class KaoQingWeixinThread implements Runnable{
	
	
	/*public KaoQingWeixinThread(){
		new Thread(this,"KaoQingWeixinThread");
	}*/
	
	@Override
	public void run() {
		
		try {
			/*int k=all 获得到员工数;
			//此处调用王工的代码
			while(i<all){
				逻辑代码
				System.out.println("threadTest=====>"+i);
				i++;
				Thread.sleep(1000*60);
			}*/
			X5Actions x5action=new X5Actions();
		/*	x5action.excuteTask("/JSPTPDERP/weixin/process/attendanceReminder/attendanceReminderProcess","mainActivity","generateAttRemAction");*/
			
			System.out.println("threadTest=====>start");
			x5action.excuteTask("/JSPTPDERP/weixin/process/attendanceReminder/attendanceReminderProcess","mainActivity","sendAttRemMessageAllAction");
			System.out.println("threadTest=====>end");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
