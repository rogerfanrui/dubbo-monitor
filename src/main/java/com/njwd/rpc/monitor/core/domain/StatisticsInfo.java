package com.njwd.rpc.monitor.core.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.monitor.MonitorService;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class StatisticsInfo extends SuperBean {

	@JsonIgnore
	private URL url;

	private String application;

	private String service;

	private String method;

	private String group;

	private String version;

	private String client;

	private String provider;
	
	private int success;
	
	private int error;
	
	private Date ctime;
	
	private int maxElapsed;
	
	private int elapsed;
	
	private int port;
	
	private String ip;

	public StatisticsInfo(URL url) {
		this.url = url;
		this.application = url.getParameter(MonitorService.APPLICATION);
		this.service = url.getParameter(MonitorService.INTERFACE);
		this.method = url.getParameter(MonitorService.METHOD);
		
		
		this.group = url.getParameter(MonitorService.GROUP,"*");
		this.version = url.getParameter(MonitorService.VERSION);
		String _client = url.getParameter(MonitorService.CONSUMER,"");
		if(StringUtils.isNotEmpty(_client)){
			this.provider = _client;
		}
		String _provider = url.getParameter(MonitorService.PROVIDER,"");
		if(StringUtils.isNotEmpty(_provider)){
			this.client = _provider;
		}
		
		this.success=url.getParameter(MonitorService.SUCCESS,0);
		this.error=url.getParameter(MonitorService.FAILURE,0);
		
		String time=url.getParameter(Constants.TIMESTAMP_KEY);
		this.ctime=StringUtils.isBlank(time)?new Date():new Date(Long.valueOf(time));
		this.maxElapsed=url.getParameter(MonitorService.MAX_ELAPSED,0);
		//this.avgElapsed=((success+error)==0)?0:url.getParameter(MonitorService.ELAPSED,0)/(success+error);
		this.elapsed=url.getParameter(MonitorService.ELAPSED,0);
		this.port=url.getPort();
		this.ip=url.getIp();
	}
	public StatisticsInfo(){}
	
	private String[] ipports(String host){
		String[] sa=	host.split(":");
		return new String[]{sa[0],sa.length>1?sa[1]:"0"};
	}
	
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getApplication() {
		return application;
	}

	public StatisticsInfo setApplication(String application) {
		this.application = application;
		return this;
	}

	public String getService() {
		return service;
	}

	public StatisticsInfo setService(String service) {
		this.service = service;
		return this;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMethod() {
		return method;
	}

	public StatisticsInfo setMethod(String method) {
		this.method = method;
		return this;
	}

	public String getClient() {
		return client;
	}

	public StatisticsInfo setClient(String client) {
		this.client = client;
		return this;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public int getMaxElapsed() {
		return maxElapsed;
	}
	public void setMaxElapsed(int maxElapsed) {
		this.maxElapsed = maxElapsed;
	}
	
	public int getElapsed() {
		return elapsed;
	}
	public void setElapsed(int elapsed) {
		this.elapsed = elapsed;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
   
}