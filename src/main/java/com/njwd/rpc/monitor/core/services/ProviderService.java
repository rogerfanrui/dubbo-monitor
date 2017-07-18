
package com.njwd.rpc.monitor.core.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.core.domain.Consumer;
import com.njwd.rpc.monitor.core.domain.Method;
import com.njwd.rpc.monitor.core.domain.Provider;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.services.api.DubboCoreServicesHandler;
import com.njwd.rpc.monitor.core.services.api.DubboMonitorHandler;
import com.njwd.rpc.monitor.core.services.api.DubboServiceHandler;
import com.njwd.rpc.monitor.core.services.api.DefaultMonitorHandler;
import com.njwd.rpc.monitor.core.services.api.ProviderServicesManager;
import com.njwd.rpc.monitor.core.util.Tool;

@Component("ProviderService")
public class ProviderService extends DefaultMonitorHandler  implements DubboCoreServicesHandler<Provider>,ProviderServicesManager{

	
	private  static ConcurrentMap<String, Set<Provider>> inMemoryServices = Maps.newConcurrentMap();
	
	@Autowired
	private MockServices mockServices;
	
	@Override
	public void actionService(URL url) {
		String servicesKey = url.getServiceKey();
		Set<Provider> consumerLists =inMemoryServices.get(servicesKey);
		 if(consumerLists == null){
			 consumerLists = Collections.synchronizedSet(new HashSet<Provider>());
			 inMemoryServices.put(servicesKey, consumerLists);
		 }
		 consumerLists.add(new Provider(url));
		
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
			for (Map.Entry<String, Set<Provider>> serviceEntry : inMemoryServices
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
		return StringUtils.isNotBlank(category)?(category.equals(Constants.PROVIDERS_CATEGORY)?true:false):false;
	}
	
	public List<Provider> findAll(){
		List<Provider> lists = Lists.newLinkedList();
		for(Set<Provider> v: inMemoryServices.values()){
			lists.addAll(Collections.unmodifiableCollection(v));
		}
		return lists;
		
	}

	@Override
	public boolean isSupportHandlerMonitor(StatisticsInfo info) {
		
		return StringUtils.isNotBlank(info.getProvider());
	}

	@Override
	public void updateServiceStaInfo(SKey skey, SValue sv) {
		
		
		
		for(Entry<String, Set<Provider>> entry:inMemoryServices.entrySet()){
			if(Tool.getInterface(entry.getKey()).equals(Tool.getInterface(skey.serviceKey))){
				
				for(Provider c:entry.getValue()){
					//定位 Ip port method
					if(c.getIp().equals(skey.ip) && c.getPort()==skey.port ){
						//&& !Constants.ANY_VALUE.equals(skey.version) 这里先暂时不判断版本号
						if (Constants.ANY_VALUE.equals(skey.group)
								|| (skey.group.equals(c.getGroup())) ) {
							
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
				
			}
		}
		
		
		
		
	}

	@Override
	public boolean mock(String services,String groupName, String mockParam) {
		groupName = StringUtils.isBlank(groupName)?"*":groupName;
		
		for(Entry<String, Set<Provider>> entry:inMemoryServices.entrySet()){
			if(Tool.getInterface(entry.getKey()).equals(services)&& Tool.getGroup(entry.getKey()).equals(groupName)){
				
				for(Provider c:entry.getValue()){
					
						mockServices.executeMock(services, mockParam,groupName);
						
						c.setMockDown(StringUtils.isNotBlank(mockParam)?true:false);
					
				}
				
			}
		}
		return true;
	}
	
}
