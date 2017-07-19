package com.njwd.rpc.monitor.core.dubbo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.monitor.MonitorService;
import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.monitor.MonitorEvent;

@Service
public class ListenerMonitorService implements MonitorService {

	private Logger log = LoggerFactory.getLogger(ListenerMonitorService.class);

	@Override
	// 这里计算时间估计会长，采用异步的方式 立即返回
	public void collect(URL statistics) {
		// 这里特别需要说明，在count://里面 没有group参数，因为在真实情况下不同的group肯定在不同的机器上
		log.debug(statistics.toFullString());
		URL _addGorupUrl = statistics.addParameter(Constants.GROUP_KEY, "*");

		MonitorEvent event = new MonitorEvent(this, _addGorupUrl);
		SpringUtils.getApplicationContext().publishEvent(event);

	}

	@Override
	public List<URL> lookup(URL query) {
		// 暂不实现
		return null;
	}

}