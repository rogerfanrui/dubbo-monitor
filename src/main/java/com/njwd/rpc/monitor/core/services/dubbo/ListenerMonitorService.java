
package com.njwd.rpc.monitor.core.services.dubbo;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.monitor.MonitorService;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;
import com.njwd.rpc.monitor.core.services.api.DubboCoreServicesHandler;
import com.njwd.rpc.monitor.core.services.api.DubboMonitorHandler;

@Service( group = "*", validation = "true",delay=-1)
public class ListenerMonitorService implements MonitorService {
	
	private Logger log = LoggerFactory.getLogger(ListenerMonitorService.class);
	
	
	
	@Resource(name="ConsumerService")
	DubboCoreServicesHandler conserives;
	@Resource(name="ProviderService")
	DubboCoreServicesHandler proservices;
	
	private DubboMonitorHandler[] exes;
	@PostConstruct
	public void init(){
		exes = new DubboMonitorHandler[]{conserives,proservices};
	}
	

	@Override
	//这里计算时间估计会长，采用异步的方式 立即返回
	public void collect(URL statistics) {
		//这里特别需要说明，在count://里面 没有group参数，因为在真实情况下不同的group肯定在不同的机器上
		URL _addGorupUrl=	statistics.addParameter(Constants.GROUP_KEY, "*");
		log.debug(_addGorupUrl.toFullString());
		StatisticsInfo sinfo = new StatisticsInfo(_addGorupUrl) ;
		for(DubboMonitorHandler e:exes){
			e.handler(sinfo);
		}
		
		
		
	}

	@Override
	public List<URL> lookup(URL query) {
		// 暂不实现
		return null;
	}

   

}