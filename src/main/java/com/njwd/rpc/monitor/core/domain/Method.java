package com.njwd.rpc.monitor.core.domain;

public class Method extends SuperBean {

	private String mName;
	
	private int success;
	
	private int fail;
	
	private Integer aveETime;
	
	private Integer maxETime;
	
	private Integer eTime;

	public Method(){}
	
	
	
	
	public Integer geteTime() {
		return eTime;
	}




	public void seteTime(Integer eTime) {
		this.eTime = eTime;
	}




	public Method(String name){
		this.mName = name;
	}
	
	
	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public Integer getAveETime() {
		return aveETime;
	}

	public void setAveETime(Integer aveETime) {
		this.aveETime = aveETime;
	}

	public Integer getMaxETime() {
		return maxETime;
	}

	public void setMaxETime(Integer maxETime) {
		this.maxETime = maxETime;
	}
	
	
	
}
