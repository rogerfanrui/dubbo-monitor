
package com.njwd.rpc.monitor.core.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.Log;
import com.alibaba.dubbo.registry.RegistryService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.core.domain.AutoMockConfig;
import com.njwd.rpc.monitor.core.domain.Consumer;
import com.njwd.rpc.monitor.core.domain.Method;
import com.njwd.rpc.monitor.core.domain.Provider;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.services.api.ConsumerServicesManager;
import com.njwd.rpc.monitor.core.services.api.DubboCoreServicesHandler;
import com.njwd.rpc.monitor.core.services.api.DubboMonitorHandler;
import com.njwd.rpc.monitor.core.services.api.DubboServiceHandler;
import com.njwd.rpc.monitor.core.services.api.DefaultMonitorHandler;
import com.njwd.rpc.monitor.core.util.Pair;
import com.njwd.rpc.monitor.core.util.Tool;

/**
 * fuck!!!!
 * 这里还要实现DubboCoreServicesHandler 完全是因为spring 在选择代理上的判断!!，如果发现targetClass 有接口的话，默认使用jdkproxy
 * 而且jdk proxy 无法代理多个接口！！
 * @see DefaultAopProxyFactory
 *
 */
@Component("ConsumerService")
public class ConsumerService extends DefaultMonitorHandler implements DubboCoreServicesHandler<Consumer>,ConsumerServicesManager{

	private static ConcurrentMap<String, Set<Consumer>> inMemoryServices = Maps.newConcurrentMap();
	
	
	@Autowired
	private MockServices mockServices;
	@Autowired
	private ConfigServices configServices;
	
	@Override
	public void actionService(URL url) {
		
		if(handlerMockPro(url)){
			return;
		}
		
		String servicesKey = url.getServiceKey();
		Set<Consumer> consumerLists =inMemoryServices.get(servicesKey);
		 if(consumerLists == null){
			 consumerLists = Collections.synchronizedSet(new HashSet<Consumer>());
			 inMemoryServices.put(servicesKey, consumerLists);
		 }
		 consumerLists.add(new Consumer(url));
		
		
	}
	
	private boolean handlerMockPro(URL url){
		boolean hander=false;
		if(url.getProtocol().equals(Constants.OVERRIDE_PROTOCOL)){
			hander=true;
			String mockKey=url.getParameter(Constants.MOCK_KEY);
			String appName=url.getParameter(Constants.APPLICATION_KEY);
			String services = url.getPath();
			for(Entry<String, Set<Consumer>> entry:inMemoryServices.entrySet()){
				if(Tool.getInterface(entry.getKey()).equals(services)){
					
					for(Consumer c:entry.getValue()){
						if(c.getAppName().equals(appName)){
							
							
						//	c.setMockDown(StringUtils.isNotBlank(mockKey)?true:false);
						}
					}
					
				}
			}
		}
		return hander;
		
	}

	@Override
	public void removeService(URL url) {
		
		String group = url.getParameter(Constants.GROUP_KEY);
		String version = url.getParameter(Constants.VERSION_KEY);
		// 注意：empty协议的group和version为*
		//TODO 这里目前为删除，这样就会导致下线的服务不能记录，未来可以考虑修正这部分情况
		if (!Constants.ANY_VALUE.equals(group)
				&& !Constants.ANY_VALUE.equals(version)) {
			inMemoryServices.remove(url.getServiceKey());
		} else {
			//这里是为了兼容不同版本的consumer
			for (Map.Entry<String, Set<Consumer>> serviceEntry : inMemoryServices
					.entrySet()) {
				String service = serviceEntry.getKey();
				if (Tool.getInterface(service).equals(
						url.getServiceInterface())
						&& (Constants.ANY_VALUE.equals(group) || com.alibaba.dubbo.common.utils.StringUtils
								.isEquals(group,
										Tool.getGroup(service)))
						&& (Constants.ANY_VALUE.equals(version) || com.alibaba.dubbo.common.utils.StringUtils
								.isEquals(version,
										Tool.getVersion(service)))) {
					inMemoryServices.remove(service);
				}
			}
		}
		
	}

	@Override
	public boolean support(String category) {
		
		return StringUtils.isNotBlank(category)?(category.equals(Constants.CONSUMERS_CATEGORY)?true:false):false;
	}
	
	
	public List<Consumer> findAll(){
		List<Consumer> lists = Lists.newLinkedList();
		for(Set<Consumer> v: inMemoryServices.values()){
			lists.addAll(Collections.unmodifiableCollection(v));
		}
		return lists;
		
	}

	@Override
	public boolean isSupportHandlerMonitor(StatisticsInfo info) {
		
		return StringUtils.isNotBlank(info.getClient());
	}

	@Override
	public void updateServiceStaInfo(SKey skey, SValue sv) {
		
		Set<Consumer> _l=	inMemoryServices.get(skey.serviceKey);
		if(_l == null){
			return;
		}
		
		for(Consumer c:_l){
			//定位 Ip port method
			if(c.getIp().equals(skey.ip) && c.getPort()==skey.port){
				for(Method m: c.getMethods()){
					if(m.getmName().equalsIgnoreCase(skey.methodName)){
						
						//处理
						m.setAveETime(sv.avgElasped);
						m.setFail(sv.error);
						m.setMaxETime(sv.maxElapsed);
						m.setSuccess(sv.success);
						m.seteTime(sv.elapsed);
						
					}
				}
			}
		}
		
	}
	
	

	@Override
	public boolean mock(String services, String appName,String ip, String mockParam) {
		for(Entry<String, Set<Consumer>> entry:inMemoryServices.entrySet()){
			if(Tool.getInterface(entry.getKey()).equals(services)){
				
				for(Consumer c:entry.getValue()){
					if(c.getAppName().equals(appName)){
						mockServices.executeMock(services, appName, ip, mockParam,null);
						
						//c.setMockDown(StringUtils.isNotBlank(mockParam)?true:false);
					}
				}
				
			}
		}
		return true;
	}
	
	
	
	@PostConstruct
	public void runnAutoDown(){
		System.out.println(this);
		//主要是为了统计一段时间的，后期扩展可以修正该段，先简单处理。
				new Thread(new AutoDownThread(),"scanner-runnAutoDown").start(); 
	
	
	
	}
	
	
	
	class AutoDownThread implements Runnable{

		@Override
		public void run() {
			while(true){
				actRun();
				try {
					Thread.sleep(60*1000l);
				} catch (InterruptedException e) {
					
				}
			}
		}
	   private void actRun(){

		List<AutoMockConfig> configs=	configServices.loadConfigs();
   		for(AutoMockConfig c:configs){
   			
   			
   			for(Entry<String, Set<Consumer>> entry:inMemoryServices.entrySet()){
   				if(Tool.getInterface(entry.getKey()).equals(c.getServiceName())){
   					
   					for(Consumer cu:entry.getValue()){
   						//if(cu.getAppName().equals(c.getAppName())&& !cu.isMockDown()){
   							if(c.getIp().equals(Constants.ANYHOST_VALUE)||c.getIp().equals(cu.getIp())){
   								if(scale(cu)>=c.getErrorScale()){
   									//执行降级
   									mock(cu.getServiceName(), cu.getAppName(), cu.getIp(), MockServices.MOCK_UPDOWN_VALUE);
   								}
   							}
   							
   							
   						//}
   					}
   					
   				}
   			}
   		}
		
	   }
	   
		
		/**
		 * 这里是针对所有METHOD进行统计，如果有必要也可以分METHOD 但是降级面向的是服务！！切记
		 * @param c
		 * @return
		 */
		private float scale(Consumer c){
			int allFails=0;
			int all=0;
			for(Method d:c.getMethods()){
				allFails +=d.getFail();
				all +=(d.getFail()+d.getSuccess());
			}
			
		  return all==0?0.0f:(allFails/all);
			
			
		}
		
		
	}
}
