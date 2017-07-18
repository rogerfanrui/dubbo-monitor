package com.njwd.rpc.monitor.core.services.api;

import java.util.List;

public interface DubboCoreServicesHandler<T> extends  DubboMonitorHandler,DubboServiceHandler{

	 List<T> findAll();
	 
	
}
