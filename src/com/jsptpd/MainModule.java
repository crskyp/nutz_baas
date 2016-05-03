package com.jsptpd;

import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@Modules(scanPackage = true)
@IocBy(type = ComboIocProvider.class, args = { "*js", "ioc/",
                                               "*quartz",
                                               "*async",
                                               "*tx",
                                               "*anoo", "com.jsptpd"})
public class MainModule {
}
