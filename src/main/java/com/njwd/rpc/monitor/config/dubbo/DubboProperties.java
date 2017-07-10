package com.njwd.rpc.monitor.config.dubbo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix ="dubbo")  
public class DubboProperties {

	private String registryAddress;
	private boolean registryCheck;
	private int registryTimeout;
	private String applicationName;
	private String annotationPackage;
	private String protocolName;
	private int  protocolPort;
	
	
	
	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	public int getProtocolPort() {
		return protocolPort;
	}
	public void setProtocolPort(int protocolPort) {
		this.protocolPort = protocolPort;
	}
	public String getRegistryAddress() {
		return registryAddress;
	}
	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}
	public boolean isRegistryCheck() {
		return registryCheck;
	}
	public void setRegistryCheck(boolean registryCheck) {
		this.registryCheck = registryCheck;
	}
	public int getRegistryTimeout() {
		return registryTimeout;
	}
	public void setRegistryTimeout(int registryTimeout) {
		this.registryTimeout = registryTimeout;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getAnnotationPackage() {
		return annotationPackage;
	}
	public void setAnnotationPackage(String annotationPackage) {
		this.annotationPackage = annotationPackage;
	}
	
	
	
}
