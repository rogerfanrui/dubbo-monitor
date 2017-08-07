package com.njwd.rpc.monitor.core.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Invoker extends SuperBean {

	@NotEmpty
	private String appName;
	@NotEmpty
	private String serviceName;
	@JSONField(name="ctime",format="yyyy-MM-dd HH:mm:ss")
	private Date ctime;
	private Integer mDownStatus=0;
	@NotEmpty
	private String ip;
	private int port;
	private String group;
	private String orignGroup;
	@JsonIgnore
	protected URL url;
	
	private String metaInfo;
	
	public Invoker(){}
	
	public Invoker(URL url){
		this.url= url;
		this.appName=url.getParameter(Constants.APPLICATION_KEY);
		this.serviceName=url.getParameter(Constants.INTERFACE_KEY,url.getPath());
		
		String time=url.getParameter(Constants.TIMESTAMP_KEY);
		ctime=StringUtils.isBlank(time)?new Date():new Date(Long.valueOf(time));
		
		this.ip=url.getHost();
		this.port=url.getPort();
		this.group = url.getParameter(Constants.GROUP_KEY,"*");
		//这个地方是为了兼容当没有设置group的时候导致的问题
		this.orignGroup= url.getParameter(Constants.GROUP_KEY);
		if(StringUtils.isBlank(this.orignGroup)){
			url.addParameter(Constants.GROUP_KEY, "*");
		}
		this.metaInfo = url.toFullString();
	}
	
	
	
	public String getOrignGroup() {
		return orignGroup;
	}

	public void setOrignGroup(String orignGroup) {
		this.orignGroup = orignGroup;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
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

	public Integer getmDownStatus() {
		return mDownStatus;
	}

	public void setmDownStatus(Integer mDownStatus) {
		this.mDownStatus = mDownStatus;
	}
	
	

	
	
}
