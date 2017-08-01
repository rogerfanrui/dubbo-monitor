package com.njwd.rpc.monitor.core.monitor.domain;

public class StaAll {

	private Long comSuccessCount = 0l;
	private Long comErrorCount = 0l;
	private Double comElapsed = 0.0d;

	private Long proSuccessCount = 0l;
	private Long proErrorCount = 0l;
	private Double proElapsed = 0.0d;

	private String servicesName;
	
	private Double score;
	
	
	
	

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getServicesName() {
		return servicesName;
	}

	public void setServicesName(String servicesName) {
		this.servicesName = servicesName;
	}

	public Long getComSuccessCount() {
		return comSuccessCount;
	}

	public void setComSuccessCount(Long comSuccessCount) {
		this.comSuccessCount = comSuccessCount;
	}

	public Long getComErrorCount() {
		return comErrorCount;
	}

	public void setComErrorCount(Long comErrorCount) {
		this.comErrorCount = comErrorCount;
	}

	public Double getComElapsed() {
		return comElapsed;
	}

	public void setComElapsed(Double comElapsed) {
		this.comElapsed = comElapsed;
	}



	public Long getProSuccessCount() {
		return proSuccessCount;
	}

	public void setProSuccessCount(Long proSuccessCount) {
		this.proSuccessCount = proSuccessCount;
	}

	public Long getProErrorCount() {
		return proErrorCount;
	}

	public void setProErrorCount(Long proErrorCount) {
		this.proErrorCount = proErrorCount;
	}

	public Double getProElapsed() {
		return proElapsed;
	}

	public void setProElapsed(Double proElapsed) {
		this.proElapsed = proElapsed;
	}



}
