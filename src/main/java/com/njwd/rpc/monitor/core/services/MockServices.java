package com.njwd.rpc.monitor.core.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.registry.RegistryService;

@Service
public class MockServices {

	public static String MOCK_UPDOWN_VALUE="force:return null";
	
	@Autowired
	private RegistryService registryService;
	
	public void executeMock(String services, String appName,String ip,String mockParam,String groupName){
		
		ip = StringUtils.isNoneBlank(ip)?ip:Constants.ANYHOST_VALUE;
		com.njwd.rpc.monitor.core.domain.Oride or = new com.njwd.rpc.monitor.core.domain.Oride();
		or.setAppName(appName);
		or.setEnabled(true);
		or.setServiceName(services);
		or.setIp(ip);
		if(StringUtils.isNoneBlank(groupName)){
			or.setGroup(groupName);
		}
		
		or.setParams("mock=" + URL.encode("force:return null"));
		if(StringUtils.isNoneBlank(mockParam)){
			registryService.register(or.toUrl());
		}else{
			registryService.unregister(or.toUrl());
		}

	}
	
	public void executeMock(String services,String mockParam,String groupName){
		executeMock(services, null, null, mockParam,groupName);
	}
}
