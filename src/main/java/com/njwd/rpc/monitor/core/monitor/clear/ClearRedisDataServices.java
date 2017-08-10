package com.njwd.rpc.monitor.core.monitor.clear;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.monitor.util.ScoreUtil;
import com.njwd.rpc.monitor.core.util.DateUtils;

@Service
public class ClearRedisDataServices {

	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	
	@PostConstruct
	public void handler(){
		//分钟保留3个月数据
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		        public void run() {
		           //秒 3个月前的score
		        	Double endScode =ScoreUtil.score(DateUtils.add(new Date(), Calendar.MONTH, -3), 1) ;
		        	Double startScoe = ScoreUtil.score(new Date(), 1);
		        	if(startScoe>endScode){
		        		log.info("时间上不需要处理3个月前数据");
		        	}
		        	
		        	try {
						Map<String, ClearRedisDatasHandlers> beans = SpringUtils
								.getApplicationContext().getBeansOfType(
										ClearRedisDatasHandlers.class);
						if (beans != null) {
							for (ClearRedisDatasHandlers h : beans.values()) {
								List<String> keyPattenrs = h
										.minutesKeysPattern();
								for (String k : keyPattenrs) {
									Set<String> keys = redisTemplate.keys(k);
									if (keys != null && !keys.isEmpty()) {
										for (String _k : keys) {
											redisTemplate
													.opsForZSet()
													.removeRangeByScore(_k,
															startScoe, endScode);
										}
									}

								}
							}
						}
					} catch (Exception e) {
						log.error("clear redis data fail",e);
					}
		        	
		        }
		}, 1*60*60*1000l , 24*60*60*1000l);
	}
}
