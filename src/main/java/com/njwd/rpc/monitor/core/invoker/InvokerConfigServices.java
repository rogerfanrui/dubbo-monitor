package com.njwd.rpc.monitor.core.invoker;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.Constants;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.njwd.rpc.monitor.core.domain.Consumer;
import com.njwd.rpc.monitor.core.domain.Empty;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.domain.Oride;
import com.njwd.rpc.monitor.core.domain.Provider;
import com.njwd.rpc.monitor.core.domain.Route;
import com.njwd.rpc.monitor.core.util.Tool;

@Service
public class InvokerConfigServices implements ApplicationListener<InvokeMetaEvent>  {

	
	 private static Table<String, String, List<Invoker>> data = HashBasedTable.create();
	@Override
	public void onApplicationEvent(InvokeMetaEvent event) {
		if(event.getSobj() == null){
			return;
		}
		Invoker invoker =event.getSobj();
		if(invoker instanceof Provider){
			add(invoker, Constants.PROVIDERS_CATEGORY);
		}else if(invoker instanceof Consumer){
			add(invoker, Constants.CONSUMERS_CATEGORY);
		}else if(invoker instanceof Route){
			add(invoker, Constants.ROUTERS_CATEGORY);
		}else if(invoker instanceof Oride){
			add(invoker, Constants.CONFIGURATORS_CATEGORY);
		}else if (invoker instanceof Empty){
			Empty empty = (Empty)invoker;
			if(empty.getCategory().equals(Constants.PROVIDERS_CATEGORY)){
				remove(invoker, Constants.PROVIDERS_CATEGORY);
			}else if(empty.getCategory().equals(Constants.CONSUMERS_CATEGORY)){
				remove(invoker, Constants.CONSUMERS_CATEGORY);
			}else if(empty.getCategory().equals(Constants.ROUTERS_CATEGORY)){
				remove(invoker, Constants.ROUTERS_CATEGORY);
			}else if(empty.getCategory().equals(Constants.CONFIGURATORS_CATEGORY)){
				remove(invoker, Constants.CONFIGURATORS_CATEGORY);
			}
		}
		
	}
	
	
	public Map<String, Map<String, List<Invoker>>> selectAll(){
		return data.rowMap(); 
	}
	
	private void add(Invoker invoker,String category){
		String servicesName = invoker.getServiceName();
		List<Invoker> invokers=	data.get(servicesName,category);
		
		if(invokers == null){
			invokers = Lists.newLinkedList();
		}
		
		for(Invoker i:invokers){
			if(Tool.isMatch(i.getUrl(), invoker.getUrl())){
				return;
			}
		}
		invokers.add(invoker);
		data.put(servicesName, category, invokers);
	}
	
	
	private void remove(final Invoker invoker,final String category){
		List<Invoker> invokers=	data.get(invoker.getServiceName(),category);
		if(invokers == null ||invokers.isEmpty()){
			return;
		}
		
		invokers=Lists.newLinkedList(Collections2.filter(invokers, new Predicate<Invoker>() {

			@Override
			public boolean apply(Invoker input) {
				
				boolean flag= Tool.isMatch(invoker.getUrl(), input.getUrl());
				return !flag;
			}
			
		}));
		//如果值为空，直接remove掉
		if(invokers.size()==0){
			data.remove(invoker.getServiceName(), category);
			
		}else{
			data.put(invoker.getServiceName(),category, invokers);
		}
	}

	
}
