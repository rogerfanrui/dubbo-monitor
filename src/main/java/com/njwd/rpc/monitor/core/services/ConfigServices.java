package com.njwd.rpc.monitor.core.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.njwd.rpc.monitor.core.domain.AutoMockConfig;

@Service
public class ConfigServices {

	
	public List<AutoMockConfig> loadConfigs(){
		List<AutoMockConfig> configs = Lists.newArrayList();
		AutoMockConfig config = new AutoMockConfig();
		config.setAppName("njwd-rpc-consumer");
		//通配IP为 0.0.0.0
		config.setIp("192.168.31.174");
		config.setServiceName("com.njwd.rpc.api.UserSimpleSerivices");
		/**
		 * 取值为大于0.1小于1
		 */
		config.setErrorScale(0.3f);
		
		configs.add(config);
		return configs;
	}
}
