package com.njwd.rpc.monitor.core.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;

public class Service extends SuperBean {


	private String appName;
	private String serviceName;
	private Date ctime;
	
	private String ip;
	private int port;
	
	public Service(){}
	
	public Service(URL url){
		this.appName=url.getParameter(Constants.APPLICATION_KEY);
		this.serviceName=url.getParameter(Constants.INTERFACE_KEY);
		String time=url.getParameter(Constants.TIMESTAMP_KEY);
		ctime=StringUtils.isBlank(time)?new Date():new Date(Long.valueOf(time));
		
		this.ip=url.getHost();
		this.port=url.getPort();
	}
	
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

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
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	
	
	
	
}
