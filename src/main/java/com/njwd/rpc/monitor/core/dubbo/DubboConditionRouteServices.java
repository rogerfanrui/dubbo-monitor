package com.njwd.rpc.monitor.core.dubbo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.registry.RegistryService;
import com.njwd.rpc.monitor.core.domain.ConditionRoute;

@Service
public class DubboConditionRouteServices {

	@Reference
	private RegistryService registryService;
	
	
	public void addConditionRule(ConditionRoute route){
		
		String path = buildPath(route);
		registryService.register(URL.valueOf(path));
		
	}
	
	public void removeCondition(String url){
		registryService.unregister(URL.valueOf(url));
	}
	
	private String buildPath(ConditionRoute route) {
		StringBuffer sb = new StringBuffer("condition://0.0.0.0/");
		sb.append(route.getServicesName()).append("?category=routers&dynamic=false&rule=");
		
		StringBuffer condition = new StringBuffer();
		if(StringUtils.isNoneBlank(route.getCustomerHostExpress())){
			condition.append(route.getCustomerHostExpress());
		}
		condition.append(" => ");
		if(StringUtils.isNoneBlank(route.getProviderHostExpress())){
			condition.append(route.getProviderHostExpress());
		}
		sb.append(URL.encode(condition.toString()));
		return sb.toString();
	}
}
