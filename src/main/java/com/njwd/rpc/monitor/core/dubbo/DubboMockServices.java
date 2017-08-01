package com.njwd.rpc.monitor.core.dubbo;

import java.util.concurrent.Executor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.registry.RegistryService;
import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.domain.CMocking;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.meta.InvokeMetaEvent;

@Service
public class DubboMockServices {

	public static String MOCK_UPDOWN_VALUE="force:return null";
	
	@Reference
	private RegistryService registryService;
	
	@Autowired
	@Qualifier("monitorAsyncExecutor")
	Executor executor;
	
	public void executeMock(final Invoker invoker,boolean isDown){
		String ip = invoker.getIp();
		ip = StringUtils.isNoneBlank(ip)?ip:Constants.ANYHOST_VALUE;
		com.njwd.rpc.monitor.core.domain.Oride or = new com.njwd.rpc.monitor.core.domain.Oride();
		or.setAppName(invoker.getAppName());
		or.setEnabled(true);
		or.setServiceName(invoker.getServiceName());
		or.setIp(ip);
		if(StringUtils.isNoneBlank(invoker.getGroup())){
			or.setGroup(invoker.getGroup());
		}
		
		or.setParams("mock=" + URL.encode("force:return null"));
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				CMocking cmocking = new CMocking();
				BeanUtils.copyProperties(invoker, cmocking);
				InvokeMetaEvent event = new InvokeMetaEvent(this,cmocking.getUrl());
				SpringUtils.getApplicationContext().publishEvent(event);
				
			}
		});
		
		if(isDown){
			registryService.register(or.toUrl());
			
		}else{
			registryService.unregister(or.toUrl());
		}

	}
	
	
}
