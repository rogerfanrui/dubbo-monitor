package com.njwd.rpc.monitor.core.services.api;

public interface ProviderServicesManager {

	
	/**
	 * 
	 * @param services
	 * @param mockParam 空为恢复
	 * @return
	 */
	public boolean mock(String services,String groupName,String mockParam);
	
}
