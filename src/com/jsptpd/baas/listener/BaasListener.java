package com.jsptpd.baas.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

public class BaasListener implements ServletContextListener {
	public static final String LOG_CONFIG = "/WEB-INF/justep.log.properties";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();

		String logConfig = servletContext.getRealPath(LOG_CONFIG);
		PropertyConfigurator.configure(logConfig);
	}

}
