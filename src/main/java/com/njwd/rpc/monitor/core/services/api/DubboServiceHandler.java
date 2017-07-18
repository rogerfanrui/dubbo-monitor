package com.njwd.rpc.monitor.core.services.api;

import com.alibaba.dubbo.common.URL;

public interface DubboServiceHandler {

	
	public void actionService(URL url);
	
	
	public void removeService(URL url);
	
	
	public boolean support(String category);
}
