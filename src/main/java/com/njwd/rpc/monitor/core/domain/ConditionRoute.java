package com.njwd.rpc.monitor.core.domain;

import org.hibernate.validator.constraints.NotEmpty;

public class ConditionRoute {
	@NotEmpty
	private String servicesName;
	private String customerHostExpress="";
	private String providerHostExpress="";
	public String getServicesName() {
		return servicesName;
	}
	public void setServicesName(String servicesName) {
		this.servicesName = servicesName;
	}
	public String getCustomerHostExpress() {
		return customerHostExpress;
	}
	public void setCustomerHostExpress(String customerHostExpress) {
		this.customerHostExpress = customerHostExpress;
	}
	public String getProviderHostExpress() {
		return providerHostExpress;
	}
	public void setProviderHostExpress(String providerHostExpress) {
		this.providerHostExpress = providerHostExpress;
	}
	
	
			
			
}
