package com.njwd.rpc.monitor.core.domain.inrelation;

import java.util.Set;




import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.njwd.rpc.monitor.core.domain.Invoker;

public class NodeDomain {

	private String id;
	@JsonProperty("name")
	private String appName;
	
	private String ip;
	private String port;
	
	@JsonIgnore
	private Set<Invoker> invokers =  Sets.newHashSet();
	
	
	public void addInvoker(Invoker invoker){
		invokers.add(invoker);
	}
	
	
	public Set<Invoker> getInvokers() {
		return invokers;
	}


	public void setInvokers(Set<Invoker> invokers) {
		this.invokers = invokers;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	
	
}
