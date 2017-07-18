package com.njwd.rpc.monitor.core.services;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.google.common.collect.Lists;
import com.njwd.rpc.monitor.core.services.api.DubboCoreServicesHandler;
import com.njwd.rpc.monitor.core.services.api.DubboServiceHandler;

@Component
public class ListenerAdapterService implements DubboServiceHandler {

	//监控的category
	private static List<String> SUPPORT_CATEGORY=Lists.newArrayList(Constants.PROVIDERS_CATEGORY,Constants.CONSUMERS_CATEGORY);
	@Resource(name="ConsumerService")
	DubboCoreServicesHandler conserives;
	@Resource(name="ProviderService")
	DubboCoreServicesHandler proservices;
	
	private DubboServiceHandler[] exes;
	@PostConstruct
	public void init(){
		exes = new DubboServiceHandler[]{conserives,proservices};
	}
	
	@Override
	public void actionService(URL url) {
		//识别override协议
		if(url.getProtocol().equals(Constants.OVERRIDE_PROTOCOL)){
			//这里是为了能够适配Mock协议给consumer进行处理
			if(StringUtils.isNoneBlank(url.getParameter(Constants.MOCK_KEY))){
				conserives.actionService(url);
				return;
			}
		}
		String category = url.getParameter(Constants.CATEGORY_KEY,
				Constants.PROVIDERS_CATEGORY);
		if(!SUPPORT_CATEGORY.contains(category)){
			return;
		}
		
		
		
		if (Constants.EMPTY_PROTOCOL.equalsIgnoreCase(url.getProtocol())) {
			removeService(url);
		}else{
			for(DubboServiceHandler e:exes){
				if(e.support(category)){
					e.actionService(url);
					break;
				}
				
			}
		}
		
	}
	
	

	@Override
	public void removeService(URL url) {
		String category = url.getParameter(Constants.CATEGORY_KEY,
				Constants.PROVIDERS_CATEGORY);
		for(DubboServiceHandler e:exes){
			if(e.support(category)){
				e.removeService(url);
				break;
			}
			
		}
		
	}
	
	

	@Override
	public boolean support(String category) {
		// 不用实现
		return false;
	}
	
}
