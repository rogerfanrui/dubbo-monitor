package com.njwd.rpc.monitor.core.services.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Async;

import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.core.domain.Provider;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.domain.SuperBean;

public abstract class DefaultMonitorHandler implements DubboMonitorHandler{

	private ConcurrentMap<SKey, Map<Long,SValue >> inMemoryServices = Maps.newConcurrentMap();
	
	
	@Async
	public void handler(StatisticsInfo info){
		//save into memory:
		if(!isSupportHandlerMonitor(info)){
			return;
		}
		SKey key = new SKey(info.getUrl().getServiceKey(), info.getIp(), info.getPort(), info.getMethod(),info.getGroup(),info.getVersion());
		 Map<Long,SValue > values =	inMemoryServices.get(key);
		 if(values == null){
			 values= Maps.newConcurrentMap();
			 inMemoryServices.put(key, values);
		 }
		 Long saveTime =covertToShow(info.getCtime());
		 SValue _rv= values.get(saveTime);
		 if(_rv == null){
			 _rv = new SValue();
			 values.put(saveTime, _rv);
		 }
		 _rv.success=_rv.success+info.getSuccess();
		 _rv.error=_rv.error+info.getError();
		 _rv.maxElapsed=_rv.maxElapsed>info.getMaxElapsed()?_rv.maxElapsed:info.getMaxElapsed();
		 _rv.elapsed=_rv.elapsed+info.getElapsed();
		 
		 //执行更新到pojo对象
		 synchronized (key.toString().intern()) {
			 SValue pushResult = new SValue();
			 for(SValue _t:values.values()){
				 pushResult.success=pushResult.success+_t.success;
				 pushResult.error=pushResult.error+_t.error;
				 pushResult.maxElapsed=pushResult.maxElapsed>_t.maxElapsed?pushResult.maxElapsed:_t.maxElapsed;
				 pushResult.elapsed=pushResult.elapsed+_t.elapsed;
				 
			 }
			 
			 pushResult.avgElasped=((pushResult.success+pushResult.error)==0)?0:pushResult.elapsed/(pushResult.success+pushResult.error);
			 updateServiceStaInfo(key, pushResult);
		}
		
		
	}
	
	
	protected  ConcurrentMap<SKey, Map<Long,SValue >> getInvokeMonitorDatas(){
		return inMemoryServices;
	}
	
	public abstract boolean isSupportHandlerMonitor(StatisticsInfo info);
	
	
	public abstract void updateServiceStaInfo(SKey key,SValue sv);
	
	
	@PostConstruct
	public void scannerMemory(){
		System.out.println(this);
		//主要是为了统计一段时间的，后期扩展可以修正该段，先简单处理。
		new Thread(new Runnable(){  
            public void run(){  
            	while(true){
            		Long cleanupTime=getCleanupTime();
            		for(Entry<SKey, Map<Long,SValue >>  ae:inMemoryServices.entrySet()){
            			 Iterator<Map.Entry<Long,SValue>> it = ae.getValue().entrySet().iterator();  
            			 while(it.hasNext()){  
            		            Map.Entry<Long,SValue> entry=it.next();  
            		            if(entry.getKey()<cleanupTime){
            		            	it.remove();
            		            }
            		        }  
            			 
            			
            		}
            		try {
						Thread.sleep(60*1000l);
					} catch (InterruptedException e) {
						
					}
            		
            	}
            	
        }},"scanner-cleanup").start(); 
	}
	
	protected class SKey extends SuperBean{
		
		public	String serviceKey;
		public	String ip;
		public	int port;
		public	String methodName;
		public String group;
		public String version;
		public SKey(String serviceKey, String ip, int port, String methodName,
				String group, String version) {
			super();
			this.serviceKey = serviceKey;
			this.ip = ip;
			this.port = port;
			this.methodName = methodName;
			this.group = group;
			this.version = version;
		}
		
		
		
		
		
	}
	
	protected	class SValue extends SuperBean{
		public	 int success;
		public int error;
		public int maxElapsed;
		public int elapsed;
		public int avgElasped;
		
	}
	
	
	private Long covertToShow(Date d){
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmm");
			return Long.valueOf(sd.format(d));
		} catch (Exception e) {
			e.printStackTrace();
			//nothing
			return null;
		}
	}
	
	private Long getCleanupTime(){
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, -30);
		return covertToShow(nowTime.getTime());
		 
	}
	
}
