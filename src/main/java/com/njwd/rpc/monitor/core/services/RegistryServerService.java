package com.njwd.rpc.monitor.core.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.RegistryService;

public class RegistryServerService implements InitializingBean, DisposableBean,
		NotifyListener {

	private Logger log = LoggerFactory.getLogger(RegistryServerService.class);

	private static final URL SUBSCRIBE = new URL(Constants.ADMIN_PROTOCOL,
			NetUtils.getLocalHost(), 0, "", Constants.INTERFACE_KEY,
			Constants.ANY_VALUE, Constants.GROUP_KEY, Constants.ANY_VALUE,
			Constants.VERSION_KEY, Constants.ANY_VALUE,
			Constants.CLASSIFIER_KEY, Constants.ANY_VALUE,
			Constants.CATEGORY_KEY, Constants.PROVIDERS_CATEGORY + ","
					+ Constants.CONSUMERS_CATEGORY + ","
					+ Constants.ROUTERS_CATEGORY + ","
					+ Constants.CONFIGURATORS_CATEGORY, Constants.ENABLED_KEY,
			Constants.ANY_VALUE, Constants.CHECK_KEY, String.valueOf(false));

	private static final AtomicLong ID = new AtomicLong();

	@Autowired
	private RegistryService registryService;
	
	@Autowired
	private ListenerAdapterService listerService;

	/**
	 * 这里的结构是：
	 *   category{consumer,provider,configurators,route}
	 *      |- servicesKey=group:/interface:version
	 *         |- id ： url
	 */
	private final ConcurrentMap<String, ConcurrentMap<String, Map<Long, URL>>> registryCache = new ConcurrentHashMap<String, ConcurrentMap<String, Map<Long, URL>>>();

	public ConcurrentMap<String, ConcurrentMap<String, Map<Long, URL>>> getRegistryCache() {
		return registryCache;
	}

	public void afterPropertiesSet() throws Exception {
		registryService.subscribe(SUBSCRIBE, this);
	}

	public void destroy() throws Exception {
		registryService.unsubscribe(SUBSCRIBE, this);
	}

	// 收到的通知对于 ，同一种类型数据（override、subcribe、route、其它是Provider），同一个服务的数据是全量的
	public void notify(List<URL> urls) {
		log.debug(Arrays.toString(urls.toArray()));
		
		if (urls == null || urls.isEmpty()) {
			return;
		}
		
		
		for (URL url : urls) {
			//admin 协议忽略
			if(url.getProtocol().equalsIgnoreCase(Constants.ADMIN_PROTOCOL)){
				continue;
			}
			//consumer & monitorservices忽略
			if(url.getProtocol().equals(Constants.CONSUMER) && url.getServiceInterface().equals("com.alibaba.dubbo.monitor.MonitorService")){
				continue;
			}
			listerService.actionService(url);
			
		}
		
	}
}
