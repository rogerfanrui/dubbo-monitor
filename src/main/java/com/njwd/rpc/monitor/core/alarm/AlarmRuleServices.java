package com.njwd.rpc.monitor.core.alarm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.domain.alarm.AlarmRule;
import com.njwd.rpc.monitor.core.monitor.MonitorEvent;
import com.njwd.rpc.monitor.core.monitor.StaTotalInvokerServices;
import com.njwd.rpc.monitor.core.monitor.domain.StaAll;
import com.njwd.rpc.monitor.core.util.DateUtils;

@Service
public class AlarmRuleServices   {
	private Interner<String> pool = Interners.newWeakInterner();
	
	private List<AlarmRule> alarmRule = null;
	@Autowired
	StaTotalInvokerServices staTotalInvokerServices;
	
	@Autowired
	RedisTemplate<String, AlarmRule> redisTemplate;
	
	public void addOrUpdateRule(AlarmRule rule){
		
		redisTemplate.opsForHash().put("alarm-rule-users",rule.getAppName()+"_"+ rule.getServicesName(), rule);
		load();
	}
	
	public List<AlarmRule> rules(){
		return Lists.newLinkedList(alarmRule);
	}

	@Async
	@EventListener
	public void handlerEvent(AlarmEvent event) {
		final StatisticsInfo sinfo =event.getInfo();
		synchronized (pool.intern(sinfo.getApplication()+"_"+sinfo.getService())) {
			List<AlarmRule> msgNotifyLists = Lists.newLinkedList();
			List<AlarmRule> rs = match(sinfo);
			if(rs !=null && !rs.isEmpty()){
				for(AlarmRule r: rs){
					Double pencent =getSerSuPencent(sinfo.getService(), r.getMinutes());
					if(pencent<r.getSpercent()){
						r.setCurSpencent(pencent);
						msgNotifyLists.add(r);
						
					}
				}
				
			}
			
			if(!msgNotifyLists.isEmpty()){
				AlarmNotifyEvent nofiyEvent = new AlarmNotifyEvent(this, sinfo, msgNotifyLists);
				SpringUtils.getApplicationContext().publishEvent(nofiyEvent);
			}
		
			
		}
		
	}
	
	private Double getSerSuPencent(String servicesName,Integer minutes){
		if(minutes == null || minutes==0){
			minutes =30;
		}
		
		Date end = new Date();
		Set<StaAll> staAlls =	staTotalInvokerServices.getReords(DateUtils.add(end, Calendar.MINUTE, 0-minutes), end, servicesName);
		Long sumCounts=0l;
		Long succesCounts=0l;
		for(StaAll s: staAlls){
			sumCounts +=(s.getComErrorCount()+s.getComSuccessCount());
			succesCounts +=s.getComSuccessCount();
		}
		return (succesCounts/sumCounts)*100d;
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
	
	private List<AlarmRule> match(StatisticsInfo sinfo){
		List<AlarmRule> rules = Lists.newLinkedList();
		for(AlarmRule r:alarmRule){
			if((r.getAppName().equals("*")||r.getAppName().equals(sinfo.getApplication()))&& (r.getServicesName().equals("*")||r.getServicesName().equals(sinfo.getService()))){
				rules.add( r);
			}
		}
		return rules;
	}
	
}
