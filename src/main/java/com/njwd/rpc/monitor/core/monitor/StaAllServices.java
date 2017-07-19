package com.njwd.rpc.monitor.core.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.monitor.domain.StaAll;

@Service
public class StaAllServices  implements ApplicationListener<MonitorEvent>{

	@Autowired
	RedisTemplate<String, StaAll> redisTemplate;
	
	private Interner<String> pool = Interners.newWeakInterner();
	
	
	@Async
	@Override
	public void onApplicationEvent(MonitorEvent event) {
		StatisticsInfo sinfo =event.getSobj();
		synchronized (pool.intern(sinfo.getService())) {
			StaAll sta =	get(event.getSobj().getService());
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
	
	private String key(String serviceName){
		
		return "sum_"+serviceName;
	}
	
	
	private StaAll get(String serviceName){
		StaAll sa =	redisTemplate.opsForValue().get(key(serviceName));
		return sa==null?new StaAll():sa;
	}
	

}
