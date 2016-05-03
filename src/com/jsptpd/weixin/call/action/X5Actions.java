package com.jsptpd.weixin.call.action;

import java.net.UnknownHostException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.justep.biz.client.Action;
import com.justep.biz.client.ActionEngine;
import com.justep.biz.client.ActionResult;
import com.justep.biz.client.ActionUtils;

public class X5Actions {
	public String login() throws DocumentException, UnknownHostException {
		// 从配置文件读取x5的服务器地址和分配给第三方接口的用户
		SAXReader reader = new SAXReader();
		Document dom = reader.read(getClass().getResource("/").getPath() + "/../x5config.xml");

		String businessServer = dom.selectSingleNode("/x5-config/business-server").getText();
		String loginName = dom.selectSingleNode("/x5-config/login-name").getText();
		String password = dom.selectSingleNode("/x5-config/password").getText();
		
		// 获得本地IP地址
		String localIP = java.net.InetAddress.getLocalHost().getHostAddress();
		
		// 初始化动作引擎
		ActionEngine.init(businessServer);
		// 登录
		String bSessionID = ActionEngine.login(loginName, ActionUtils.md5(password), localIP, null);
		// 返回bSessionID
		return bSessionID;
	}
	
	public  boolean  excuteTask(String process,String activity,String name) throws UnknownHostException, DocumentException{
		String bSessionID = login();
		SAXReader reader = new SAXReader();
		Document dom = reader.read(getClass().getResource("/").getPath() + "/../x5config.xml");
		
		try {
			Action action = new Action();
			// 指定动作的process、activity和action，这里要注意登录的用户应该有执行这个功能中的这个动作的权限
			action.setProcess(process);
			action.setActivity(activity);
			action.setName(name);		
			String ExecuteContext=dom.selectSingleNode("/x5-config/ExecuteContext").getText();
			/*action.setParameter("ExecuteContext", ExecuteContext);*/
			action.setExecuteContext(ExecuteContext);
			// 调用动作
			ActionResult actionResult = ActionEngine.invokeAction(action, ActionUtils.JSON_CONTENT_TYPE, bSessionID, null, null);
			// 判断是否调用成功
			if (actionResult.isSuccess()){
				// 返回值
				return actionResult.isSuccess();
			}else{
				throw new RuntimeException(actionResult.getMessage());
			}
		} finally {
			// 要保证注销，否则会占用在线人数
			ActionEngine.logout(bSessionID);
		}
	}
}
