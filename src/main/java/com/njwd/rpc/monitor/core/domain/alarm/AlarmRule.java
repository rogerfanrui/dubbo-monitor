package com.njwd.rpc.monitor.core.domain.alarm;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.njwd.rpc.monitor.core.domain.Invoker;


public class AlarmRule {
	@NotNull
	private String servicesName;
	@NotNull
	private String appName;
	
	private Date addTime = new Date();
	
	private String userName;
	private String mobiles;
	private String email;
	@NotNull
	private Integer minutes;
	@NotNull
	private Double spercent;
	
	@JsonIgnore
	private Double curSpencent;
	
	
	public Double getCurSpencent() {
		return curSpencent;
	}
	public void setCurSpencent(Double curSpencent) {
		this.curSpencent = curSpencent;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobiles() {
		return mobiles;
	}
	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getMinutes() {
		return minutes;
	}
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}
	public Double getSpercent() {
		return spercent;
	}
	public void setSpercent(Double spercent) {
		this.spercent = spercent;
	}
	
	public String getServicesName() {
		return servicesName;
	}
	public void setServicesName(String servicesName) {
		this.servicesName = servicesName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
	
	
	
	
	
	
	
}
