package com.njwd.rpc.monitor.core.alarm;

import org.springframework.context.ApplicationEvent;

import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.monitor.domain.StaAll;

public class AlarmEvent extends ApplicationEvent {

	private StatisticsInfo info;
	
	private StaAll sta;
	//按照分钟触发
	public AlarmEvent(Object source,StatisticsInfo info,StaAll sta) {
		super(source);
		this.info = info;
		this.sta = sta;
	}
	public StatisticsInfo getInfo() {
		return info;
	}
	public void setInfo(StatisticsInfo info) {
		this.info = info;
	}
	public StaAll getSta() {
		return sta;
	}
	public void setSta(StaAll sta) {
		this.sta = sta;
	}
	
	

}
