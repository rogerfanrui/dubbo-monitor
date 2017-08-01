package com.njwd.rpc.monitor.core.monitor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.alarm.AlarmEvent;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.monitor.domain.StaAll;
import com.njwd.rpc.monitor.core.monitor.util.ScoreUtil;

@Service
public class StaTotalInvokerServices  {

	private static int min=1;
	private static int hour=2;
	
	@Autowired
	RedisTemplate<String, StaAll> redisTemplate;
	
	
	
	private Map<String, StaAll> cache = Maps.newConcurrentMap();
	
	private Interner<String> pool = Interners.newWeakInterner();
	@Async
	@EventListener
	public void handelrMonitorEvent(MonitorEvent event) {
		StatisticsInfo sinfo =event.getSobj();
		
		
		handler(sinfo, min);
		handler(sinfo, hour);
		
	}
	
	
	private void handler(StatisticsInfo sinfo,int type){
		String key = key(sinfo.getService(), type);
		synchronized (pool.intern(key)) {
			StaAll sta =	getLast(sinfo, type);
			if(sinfo.isComsumer()){
				sta.setComElapsed(sta.getComElapsed()+sinfo.getElapsed());
				sta.setComErrorCount(sta.getComErrorCount()+sinfo.getError());
				sta.setComSuccessCount(sta.getComSuccessCount()+sinfo.getSuccess());
			}else{
				sta.setProElapsed(sta.getProElapsed()+sinfo.getElapsed());
				sta.setProErrorCount(sta.getProErrorCount()+sinfo.getError());
				sta.setProSuccessCount(sta.getProSuccessCount()+sinfo.getSuccess());
			}

			//先删除
			this.redisTemplate.opsForZSet().removeRangeByScore(key, sta.getScore(), sta.getScore());
			this.redisTemplate.opsForZSet().add(key, sta, sta.getScore());
			
		}
		
		
	}
	public Set<StaAll> getReords(Date startTime,Date endTime,String servicesName){
		Preconditions.checkNotNull(startTime);
		Preconditions.checkNotNull(endTime);
		Preconditions.checkNotNull(servicesName);
		
		String key = key(servicesName, min);
		Double startSocre = this.getScore(startTime, min);
		Double endScore = this.getScore(endTime, min);
		return redisTemplate.opsForZSet().reverseRangeByScore(key, startSocre, endScore);
	}
	private StaAll getLast(final StatisticsInfo info,int type){
		String key = key(info.getService(), type);
		Double soce =getScore(info.getCtime(), type);
		StaAll _result = cache.get(key);
		if( _result== null ){
			
			Set<StaAll> staInvokers =redisTemplate.opsForZSet().reverseRangeByScore(key, soce, soce);
			if(staInvokers == null || staInvokers.isEmpty() ||!(staInvokers.iterator().next().getScore().equals(soce))){
				_result = new  StaAll();
				_result.setServicesName(info.getService());
				_result.setScore(soce);
				
			}else{
				_result = staInvokers.iterator().next();
			}
			cache.put(key, _result);
		} else if (!_result.getScore().equals(soce)){
			//这里相当于切换KEY 只有切换key的时候发送监控事件，否则触发太频繁
			_result =cacheAdd(info, soce, type, key);
			
			//向监控层发送事件
			AlarmEvent event = new AlarmEvent(this, info,_result);
			SpringUtils.getApplicationContext().publishEvent(event);
			
				
			
		}
		return _result;
		
	}
	
	private StaAll cacheAdd(StatisticsInfo info,Double score,int type,String key){
		StaAll 	_result = new  StaAll();
		_result.setServicesName(info.getService());
		_result.setScore(score);
		cache.put(key, _result);
		return _result;
	}
	/**
	 * 计算score 用户排序 将redis中的sortedSet 排序
	 * @param info
	 * @param type
	 * @return
	 */
	private Double getScore(Date d ,int type){
		return ScoreUtil.score(d, type);
	}
	
	private String key(String servicesName,int type){
		return "rec_"+(type==1?"min":"hour")+"_"+servicesName;
	}
	
	
	

}
