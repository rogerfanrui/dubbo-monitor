package com.njwd.rpc.monitor.core.alarm;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.domain.alarm.AlarmRule;

public class AlarmNotifyEvent extends ApplicationEvent{

	private StatisticsInfo sinfo;
	private List<AlarmRule> rules ;
	
	public AlarmNotifyEvent(Object source,StatisticsInfo sinfo,List<AlarmRule> rules ) {
		super(source);
		this.sinfo = sinfo;
		this.rules = rules;
	}

	public StatisticsInfo getSinfo() {
		return sinfo;
	}

	public void setSinfo(StatisticsInfo sinfo) {
		this.sinfo = sinfo;
	}

	public List<AlarmRule> getRules() {
		return rules;
	}

	public void setRules(List<AlarmRule> rules) {
		this.rules = rules;
	}

	
	
	

}
