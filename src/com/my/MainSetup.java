package com.my;

import org.nutz.integration.quartz.NutQuartzCronJobFactory;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

public class MainSetup implements Setup {

    public void destroy(NutConfig nc) {
    }

    public void init(NutConfig nc) {
        NutIoc ioc = (NutIoc) nc.getIoc();
        PropertiesProxy conf = ioc.get(PropertiesProxy.class, "conf");
        conf.put("cron.pkgs", getClass().getPackage().getName());
        ioc.get(NutQuartzCronJobFactory.class);
    }

}
