package com.jsptpd;

import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@Modules(scanPackage = true)
@IocBy(type = ComboIocProvider.class, args = { "*js", "*quartz" })
public class MainModule {

}
