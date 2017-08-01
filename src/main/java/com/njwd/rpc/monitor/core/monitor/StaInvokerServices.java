package com.njwd.rpc.monitor.core.monitor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.alarm.AlarmEvent;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.monitor.domain.StaInvoker;
import com.njwd.rpc.monitor.core.monitor.util.ScoreUtil;

@Service
public class StaInvokerServices  {

	private static int min=1;
	private static int hour=2;
	
	@Autowired
	RedisTemplate<String, StaInvoker> redisTemplate;
	
	
	
	private Map<String, StaInvoker> cache = Maps.newConcurrentMap();
	
	private Interner<String> pool = Interners.newWeakInterner();
	@Async
	@EventListener
	public void handelrMonitorEvent(MonitorEvent event) {
		StatisticsInfo sinfo =event.getSobj();
		
		
		handler(sinfo, min);
		handler(sinfo, hour);
		
	}
	
	
	private void handler(StatisticsInfo info,int type){
		String key = key(info, type);
		synchronized (pool.intern(key)) {
			StaInvoker invoker =	getLast(info, type);
			invoker.setElapsed(invoker.getElapsed()+info.getElapsed());
			invoker.setErrorCount(invoker.getErrorCount()+info.getError());
			invoker.setSuccessCount(invoker.getSuccessCount()+info.getSuccess());
			//先删除
			this.redisTemplate.opsForZSet().removeRangeByScore(key, invoker.getScore(), invoker.getScore());
			this.redisTemplate.opsForZSet().add(key, invoker, invoker.getScore());
			
		}
		
		
	}
	
	private StaInvoker getLast(final StatisticsInfo info,int type){
		String key = key(info, type);
		Double soce =getScore(info, type);
		StaInvoker _result = cache.get(key);
		if( _result== null ){
			
			Set<StaInvoker> staInvokers =redisTemplate.opsForZSet().reverseRangeByScore(key, soce, soce);
			if(staInvokers == null || staInvokers.isEmpty() ||!(staInvokers.iterator().next().getScore().equals(soce))){
				_result = new  StaInvoker();
				_result.setAppName(info.getApplication());
				_result.setIp(info.getIp());
				_result.setPort(info.getPort());
				_result.setServiceName(info.getService());
				_result.setTime(ScoreUtil.scoreDate(info.getCtime(), type));
				_result.setScore(soce);
				
			}else{
				_result = staInvokers.iterator().next();
			}
			cache.put(key, _result);
		} else if (!_result.getScore().equals(soce)){
			//这里相当于切换KEY
			_result =cacheAdd(info, soce, type, key);
			
			
				
			
		}
		return _result;
		
	}
	
	private StaInvoker cacheAdd(StatisticsInfo info,Double score,int type,String key){
		StaInvoker 	_result = new  StaInvoker();
		_result.setAppName(info.getApplication());
		_result.setIp(info.getIp());
		_result.setPort(info.getPort());
		_result.setServiceName(info.getService());
		_result.setTime(ScoreUtil.scoreDate(info.getCtime(), type));
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
	private Double getScore(StatisticsInfo info,int type){
		return ScoreUtil.score(info.getCtime(), type);
	}
	
	private String key(StatisticsInfo info,int type){
		return "rec_"+pre(info)+"_"+(type==1?"min":"hour")+"_"+info.getService()+"_"+info.getIp()+"_"+info.getPort();
	}
	private String pre(StatisticsInfo info){
		return info.isComsumer()?"com":"pro";
	}
	
	

}
