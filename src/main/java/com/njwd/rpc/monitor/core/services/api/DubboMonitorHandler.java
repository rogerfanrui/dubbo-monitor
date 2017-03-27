package com.njwd.rpc.monitor.core.services.api;

import com.njwd.rpc.monitor.core.domain.StatisticsInfo;

public interface DubboMonitorHandler  {

	public void handler(StatisticsInfo info);
}
