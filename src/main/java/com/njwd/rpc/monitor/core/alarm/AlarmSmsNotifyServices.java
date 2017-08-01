package com.njwd.rpc.monitor.core.alarm;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.njwd.rpc.monitor.core.domain.alarm.AlarmRule;

@Service
public class AlarmSmsNotifyServices {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	StringRedisTemplate redisTemplate;
	
	
	@Async
	@EventListener
	public void handlerSms(AlarmNotifyEvent event){
		List<AlarmRule>  arules =event.getRules();
		if(arules != null){
			for(AlarmRule au:arules){
				this.sendSms(au);
			}
		}
	}
	
	
	
	public void sendSms(AlarmRule rule){
		String key = key(rule);
		String _flag =this.redisTemplate.opsForValue().get(key);
		if(StringUtils.isNotBlank(_flag)){
			log.info("{}已 发送过，不在重复发送");
			return;
		}
		//TODO 这里发送短信
		
		log.info("监控报警，应用名称为:{},服务名称为:{},当前成功率为:{}",rule.getAppName(),rule.getServicesName(),rule.getCurSpencent());
		//默认30分钟之内不在重复发送
		this.redisTemplate.opsForValue().set(key, "1", 30, TimeUnit.MINUTES);
	}
	
	private String key(AlarmRule rule){
		return "alarm_sms_"+rule.getAppName()+"_"+rule.getServicesName()+"_"+rule.getMobiles();
	}
}
