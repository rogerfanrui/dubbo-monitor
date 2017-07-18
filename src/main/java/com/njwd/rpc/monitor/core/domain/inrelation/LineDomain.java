package com.njwd.rpc.monitor.core.domain.inrelation;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class LineDomain {

	private String source;
	
	private String target ;
	
	private String value;
	
	private List<String> services = Lists.newLinkedList();
	
	
	public void removeServices(String servicesName){
		services.remove(servicesName);
	}
	
	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	public void addServices(String s){
		if(!services.contains(s)){
			services.add(s);
		}
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
