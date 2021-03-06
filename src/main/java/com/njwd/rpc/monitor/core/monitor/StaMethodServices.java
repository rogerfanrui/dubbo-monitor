package com.njwd.rpc.monitor.core.monitor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.monitor.clear.ClearRedisDatasHandlers;
import com.njwd.rpc.monitor.core.monitor.domain.StaInvoker;
import com.njwd.rpc.monitor.core.monitor.domain.StaMethod;
import com.njwd.rpc.monitor.core.monitor.util.ScoreUtil;

@Service
public class StaMethodServices  implements ClearRedisDatasHandlers{

	private static int min=1;
	private static int hour=2;
	
	@Autowired
	RedisTemplate<String, StaMethod> redisTemplate;
	
	private Map<String, StaMethod> cache = Maps.newConcurrentMap();
	
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
			StaMethod invoker =	getLast(info, type);
			invoker.setElapsed(invoker.getElapsed()+info.getElapsed());
			invoker.setErrorCount(invoker.getErrorCount()+info.getError());
			invoker.setSuccessCount(invoker.getSuccessCount()+info.getSuccess());
			//先删除
			this.redisTemplate.opsForZSet().removeRangeByScore(key, invoker.getScore(), invoker.getScore());
			this.redisTemplate.opsForZSet().add(key, invoker, invoker.getScore());
			
		}
		
		
	}
	
	private StaMethod getLast(StatisticsInfo info,int type){
		String key = key(info, type);
		Double soce =getScore(info, type);
		StaMethod _result = cache.get(key);
		if( _result== null ){
			
			Set<StaMethod> staInvokers =redisTemplate.opsForZSet().reverseRangeByScore(key, soce, soce);
			if(staInvokers == null || staInvokers.isEmpty() ||!(staInvokers.iterator().next().getScore().equals(soce))){
				_result = new  StaMethod();
				_result.setAppName(info.getApplication());
				_result.setIp(info.getIp());
				_result.setPort(info.getPort());
				_result.setServiceName(info.getService());
				_result.setTime(ScoreUtil.scoreDate(info.getCtime(), type));
				_result.setScore(soce);
				_result.setMethodName(info.getMethod());
				
			}else{
				_result = staInvokers.iterator().next();
			}
			cache.put(key, _result);
		} else if (!_result.getScore().equals(soce)){
			_result =cacheAdd(info, soce, type, key);
		}
		return _result;
		
	}
	
	private StaMethod cacheAdd(StatisticsInfo info,Double score,int type,String key){
		StaMethod 	_result = new  StaMethod();
		_result.setAppName(info.getApplication());
		_result.setIp(info.getIp());
		_result.setPort(info.getPort());
		_result.setServiceName(info.getService());
		_result.setTime(ScoreUtil.scoreDate(info.getCtime(), type));
		_result.setScore(score);
		_result.setMethodName(info.getMethod());
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
		return "rec_method_"+pre(info)+"_"+(type==1?"min":"hour")+"_"+info.getService()+"_"+info.getMethod()+"_"+info.getIp()+"_"+info.getPort();
	}
	private String pre(StatisticsInfo info){
		return info.isComsumer()?"com":"pro";
	}


	@Override
	public List<String> minutesKeysPattern() {
		List<String>  result = Lists.newLinkedList();
		result.add("rec_method_com_min_*");
		result.add("rec_method__pro_min_*");
		return result;
	}


	@Override
	public List<String> hoursKeysPattern() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
