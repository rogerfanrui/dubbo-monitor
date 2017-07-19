package com.njwd.rpc.monitor.core.monitor.domain;

import java.util.Date;

public class StaInvoker {

	private String time;
	private Long successCount = 0l;
	private Long errorCount =0l;
	private Double elapsed =0d;
	private String appName;
	private String ip;
	private Integer port;
	private String serviceName;

	private Double score;
	
	
	
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Long getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Long successCount) {
		this.successCount = successCount;
	}

	public Long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Long errorCount) {
		this.errorCount = errorCount;
	}

	public Double getElapsed() {
		return elapsed;
	}

	public void setElapsed(Double elapsed) {
		this.elapsed = elapsed;
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

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
