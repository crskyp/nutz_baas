package com.jsptpd.quartz;

import java.util.Date;

import org.nutz.integration.quartz.annotation.Scheduled;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@IocBean
@Scheduled(cron="0 0/5 * * * *")// 5分钟执行一次
public class QuartzTaskService implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(new Date());
	}
}
