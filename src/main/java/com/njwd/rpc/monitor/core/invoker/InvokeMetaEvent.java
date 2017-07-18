package com.njwd.rpc.monitor.core.invoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

import com.alibaba.dubbo.common.URL;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.dubbo.DubboMetaDomainFactory;

public class InvokeMetaEvent extends ApplicationEvent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4294233313111002384L;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private URL ur;
	private Invoker sobj;
	public InvokeMetaEvent(Object source) {
		super(source);
	}
	
	public InvokeMetaEvent(Object source,URL url) {
		super(source);
		this.ur = url;
		Invoker  s =	DubboMetaDomainFactory.createMetaInfo(url);
		this.sobj = s;
		if(s == null){
			log.warn("未能识别出当前协议,{}",url.toFullString());
		}
		
	}
	
	public Invoker getSobj() {
		return sobj;
	}

	public URL getUr() {
		return ur;
	}
	
	

}
