package com.njwd.rpc.monitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;

@Configuration
@ImportResource(locations="classpath:/dubbo/dubbo-monitor.xml")
@Order(1)
public class DubboConfig {

}
