package com.njwd.rpc.monitor.core.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.njwd.rpc.monitor.core.domain.CMocking;
import com.njwd.rpc.monitor.core.domain.Consumer;
import com.njwd.rpc.monitor.core.domain.Empty;
import com.njwd.rpc.monitor.core.domain.Oride;
import com.njwd.rpc.monitor.core.domain.Provider;
import com.njwd.rpc.monitor.core.domain.Route;
import com.njwd.rpc.monitor.core.domain.Invoker;

public class DubboMetaDomainFactory {

	public static Invoker createMetaInfo(URL url){
		String proto = url.getProtocol();
		String side = url.getParameter("side");
		if(proto.equals(Constants.ROUTERS_CATEGORY)||proto.equals(Constants.ROUTE_PROTOCOL) ){
			return new Route(url);
		}else if (proto.equals(Constants.EMPTY_PROTOCOL)){
			return new Empty(url);
		}else if (proto.equals(Constants.CONSUMER_PROTOCOL)){
			return new Consumer(url);
		}else if (proto.equals(Constants.OVERRIDE_PROTOCOL)){
			return new Oride(url);
		}else if (side !=null && side.equals(Constants.PROVIDER_SIDE)){
			return new Provider(url);
		}else if (proto !=null && proto.equals(CMocking.PROTOCOL)){
			return new CMocking(url);
		}
		return null;
	}
	
}
