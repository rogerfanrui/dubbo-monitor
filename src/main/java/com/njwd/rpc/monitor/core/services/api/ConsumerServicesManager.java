package com.njwd.rpc.monitor.core.services.api;


public interface ConsumerServicesManager {

	/**
	 * 
	 * @param services
	 * @param appName
	 * @param mockParam 空为恢复
	 * @return
	 */
	public boolean mock(String services,String appName,String ip,String mockParam);
	
	
	
	
	
}
