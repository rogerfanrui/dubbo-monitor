package com.njwd.rpc.monitor.core.domain;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;

/**
 * 代表降级处理中协议
 * @author Administrator
 *
 */
public class CMocking  extends Invoker{
	
	
	

	public CMocking() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CMocking(URL url) {
		super(url);
		// TODO Auto-generated constructor stub
	}
	public static String PROTOCOL="cmoking";
	@Override
	public URL getUrl() {
		URL url = new URL(PROTOCOL, super.getIp(), super.getPort(),super.getServiceName(),Constants.SERVER_KEY,super.getServiceName(), Constants.GROUP_KEY,super.getGroup(),Constants.APPLICATION_KEY,super.getAppName());
		return url;
	}

	
}
