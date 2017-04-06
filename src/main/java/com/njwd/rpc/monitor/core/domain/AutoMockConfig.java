package com.njwd.rpc.monitor.core.domain;

public class AutoMockConfig extends SuperBean {

	
	private String appName;
	private String serviceName;
	private String ip;
	
	/**
	 * 出错比例
	 */
	private float errorScale;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public float getErrorScale() {
		return errorScale;
	}

	public void setErrorScale(float errorScale) {
		this.errorScale = errorScale;
	}
	
	
}
