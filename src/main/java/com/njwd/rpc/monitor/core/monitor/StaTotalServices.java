package com.njwd.rpc.monitor.core.monitor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Sets;
import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.alarm.AlarmEvent;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.monitor.domain.StaAll;

@Service
public class StaTotalServices  {

	@Autowired
	RedisTemplate<String, StaAll> redisTemplate;
	

	
	private Interner<String> pool = Interners.newWeakInterner();
	
	@Async
	@EventListener
	public void handelrMonitorEvent(MonitorEvent event) {
		final StatisticsInfo sinfo =event.getSobj();
		synchronized (pool.intern(sinfo.getService())) {
			final StaAll sta =	get(event.getSobj().getService());
			sta.setServicesName(event.getSobj().getService());
			if(sinfo.isComsumer()){
				sta.setComElapsed(sta.getComElapsed()+sinfo.getElapsed());
				sta.setComErrorCount(sta.getComErrorCount()+sinfo.getError());
				sta.setComSuccessCount(sta.getComSuccessCount()+sinfo.getSuccess());
			}else{
				sta.setProElapsed(sta.getProElapsed()+sinfo.getElapsed());
				sta.setProErrorCount(sta.getProErrorCount()+sinfo.getError());
				sta.setProSuccessCount(sta.getProSuccessCount()+sinfo.getSuccess());
			}
			this.redisTemplate.opsForValue().set(key(sinfo.getService()), sta);
			
			

			
        }
		
		
		
		
	}
	
	
	
	
	
	public Set<String> getStaServices(){
		return redisTemplate.keys("sum_*");
	}
	
	/**
	 * 设立设计的失误 应该采用map这种格式存储
	 * 否则会导致N+1的查询
	 * @param key
	 * @return
	 */
	public Set<StaAll> selectInfosByKeys(final Set<String>  key){
		
		Set<StaAll> sets = Sets.newLinkedHashSet();
		if(key == null|| key.isEmpty()){
			return sets;
		}
		for(String k:key){
			StaAll result =this.redisTemplate.opsForValue().get("sum_"+k);
			if(result !=null){
				sets.add(result);
			}
			
		}
		return sets;
	}
	
	private String key(String serviceName){
		
		return "sum_"+serviceName;
	}
	
	
	private StaAll get(String serviceName){
		StaAll sa =	redisTemplate.opsForValue().get(key(serviceName));
		return sa==null?new StaAll():sa;
	}
	

}
