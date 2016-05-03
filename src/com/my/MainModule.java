package com.my;

import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@Modules(scanPackage = true)
@IocBy(type = ComboIocProvider.class, args = { "*js", "ioc/", "*quartz", "*async", "*tx", "*anno", "com.jsptpd" })
@SetupBy(value = MainSetup.class)
public class MainModule {
}
