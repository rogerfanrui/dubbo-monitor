package com.njwd.rpc.monitor.core.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

import com.alibaba.dubbo.common.URL;
import com.njwd.rpc.monitor.core.domain.StatisticsInfo;

public class MonitorEvent extends ApplicationEvent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4294233313111002384L;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private URL ur;
	private StatisticsInfo sobj;
	public MonitorEvent(Object source) {
		super(source);
	}
	
	public MonitorEvent(Object source,URL url) {
		super(source);
		this.ur = url;
		StatisticsInfo  s =new StatisticsInfo(url);
		this.sobj = s;
		
		
	}
	
	public StatisticsInfo getSobj() {
		return sobj;
	}

	public URL getUr() {
		return ur;
	}
	
	

}
