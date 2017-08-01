package com.njwd.rpc.monitor.core.alarm;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.domain.alarm.AlarmRule;
import com.njwd.rpc.monitor.core.monitor.MonitorEvent;
import com.njwd.rpc.monitor.core.monitor.domain.StaAll;

@Service
public class AlarmRuleServices  implements ApplicationListener<AlarmEvent> {
	private Interner<String> pool = Interners.newWeakInterner();
	
	private List<AlarmRule> alarmRule = null;
	@Autowired
	RedisTemplate<String, AlarmRule> redisTemplate;
	
	public void addOrUpdateRule(AlarmRule rule){
		
		redisTemplate.opsForHash().put("alarm-rule-users",rule.getAppName()+"_"+ rule.getServicesName(), rule);
		load();
	}
	
	public List<AlarmRule> rules(){
		return Lists.newLinkedList(alarmRule);
	}

	@Override
	public void onApplicationEvent(AlarmEvent event) {
		final StatisticsInfo sinfo =event.getInfo();
		final StaAll all =event.getSta();
		synchronized (pool.intern(sinfo.getApplication()+"_"+sinfo.getService())) {
			AlarmRule r = match(sinfo);
			if(r !=null){
				
			}
		}
		
	}
	@PostConstruct
	public void load(){
		List values =redisTemplate.opsForHash().values("alarm-rule-users");
		if(values !=null){
			alarmRule=  (List<AlarmRule>)values;
		}else{
			alarmRule= Lists.newLinkedList();
		}
	}
	
	private AlarmRule match(StatisticsInfo sinfo){
		for(AlarmRule r:alarmRule){
			if((r.getAppName().equals("*")||r.getAppName().equals(sinfo.getApplication()))&& (r.getServicesName().equals("*")||r.getServicesName().equals(sinfo.getService()))){
				return r;
			}
		}
		return null;
	}
	
}
