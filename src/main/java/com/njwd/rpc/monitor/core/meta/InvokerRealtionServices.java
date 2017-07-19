package com.njwd.rpc.monitor.core.meta;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.Constants;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.njwd.rpc.monitor.core.domain.Consumer;
import com.njwd.rpc.monitor.core.domain.Empty;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.domain.Provider;
import com.njwd.rpc.monitor.core.domain.inrelation.InvokerRealationDomain;
import com.njwd.rpc.monitor.core.domain.inrelation.LineDomain;
import com.njwd.rpc.monitor.core.domain.inrelation.NodeDomain;
import com.njwd.rpc.monitor.core.util.IdFactory;
import com.njwd.rpc.monitor.core.util.Tool;

@Service
public class InvokerRealtionServices implements ApplicationListener<InvokeMetaEvent>{
	private Logger log = LoggerFactory.getLogger(InvokerRealtionServices.class);
	private InvokerRealationDomain data = new InvokerRealationDomain(); 
	
	private Set<Provider> providers = Sets.newConcurrentHashSet();

	private Set<Consumer> consumer = Sets.newConcurrentHashSet();
	
	@Override
	public void onApplicationEvent(InvokeMetaEvent event) {
		if(event.getSobj() == null){
			return;
		}
		Invoker invoker =event.getSobj();
		if(invoker instanceof Provider){
			NodeDomain _n=		add(invoker);
			List<Invoker> ps =	searchConsumers(invoker);
			
			for(Invoker i:ps){
				NodeDomain  _nd =data.searchNodeDomain(i.getAppName());
				if(_nd == null){
					log.warn("无法匹配 consumer 与 node的关联,provider:{}",i.getUrl().toFullString());
					continue;
				}
				searchAndAddLineDomain(_nd.getId(),_n.getId() , invoker.getServiceName());
				
			}
			providers.add((Provider)invoker);
		}else if(invoker instanceof Consumer){
			NodeDomain _n=	add(invoker);
			List<Invoker> ps =	searchProvicer(invoker);
			
			for(Invoker i:ps){
				NodeDomain  _nd =data.searchNodeDomain(i.getAppName());
				if(_nd == null){
					log.warn("无法匹配 provider 与 node的关联,provider:{}",i.getUrl().toFullString());
					continue;
				}
				searchAndAddLineDomain(_n.getId(), _nd.getId(), invoker.getServiceName());
				
			}
			consumer.add((Consumer)invoker);
			
		}else if (invoker instanceof Empty){
			Empty empty = (Empty)invoker;
			if(empty.getCategory().equals(Constants.PROVIDERS_CATEGORY)){
				
				removeNodeDomain(empty);
				removeCurrentInfo(empty);
				
			}else if(empty.getCategory().equals(Constants.CONSUMERS_CATEGORY)){
				removeNodeDomain(empty);
				removeCurrentInfo(empty);
			}
		}
		
	}
	
	public void removeCurrentInfo(Empty empty){
		Set<? extends Invoker> sets  = null;
		if(empty.getCategory().equals(Constants.PROVIDERS_CATEGORY)){
			
			sets = this.providers;
		}else if(empty.getCategory().equals(Constants.CONSUMERS_CATEGORY)){
			sets = this.consumer;
		}
		Iterator<? extends  Invoker> invokers = sets.iterator();
		while(invokers.hasNext()){
			Invoker i=	invokers.next();
			if(Tool.isMatch(empty.getUrl(), i.getUrl())){
				invokers.remove();
			}
		}
		
	}
	

	public NodeDomain removeNodeDomain(Empty invoke){
		Iterator<NodeDomain> nodesi = this.data.getNodes().iterator();
		NodeDomain _n=null;
		while(nodesi.hasNext()){
			_n=	nodesi.next();
			//if cacnel的原因的 empty:// 这个协议中 appName是空
			//if(_n.getAppName().equalsIgnoreCase(invoke.getAppName())){
				Iterator<Invoker> invokers = _n.getInvokers().iterator();
				while(invokers.hasNext()){
					Invoker i=	invokers.next();
					//if(invoke.getClass().getName().equals(i.getClass().getName())){
						if(Tool.isMatch(i.getUrl(), invoke.getUrl())){
							invokers.remove();
						}
					//}
				}
				
				//如果invokers 已经为空 直接remove掉这个节点
				if( _n.getInvokers().size()==0){
					nodesi.remove();
					remoteLine(invoke, 1, _n.getId());
				}else{
					remoteLine(invoke, 0, _n.getId());
				}
			//}
		}
		
		return _n;

	}
	
	private void remoteLine(Empty empty,int type,String nodeId){
		List<LineDomain> result = null;
		if(empty.getCategory().equals(Constants.PROVIDERS_CATEGORY)){
			result=	this.data.searchLineDomainList(null, nodeId);
		}else if(empty.getCategory().equals(Constants.CONSUMERS_CATEGORY)){
			result=	this.data.searchLineDomainList(nodeId, null);
		}
		//删除服务名
		if(type == 0){
			for(LineDomain line:result){
				line.removeServices(empty.getServiceName());
			}
		}
		//删除节点的情况下 整个删除
		else if (type ==1){
			for(LineDomain line:result){
				Iterator<LineDomain> lines = data.getLines().iterator();
				while(lines.hasNext()){
					LineDomain ld = lines.next();
					if(ld.getSource().equals(line.getSource())&& ld.getTarget().equals(line.getTarget())){
						lines.remove();
					}
				}
			}
			
		} 
		
	}
	
	private NodeDomain add(Invoker invoker){
		NodeDomain nd =	data.searchNodeDomain(invoker.getAppName());
		if(nd == null){
			nd = new NodeDomain();
			nd.setAppName(invoker.getAppName());
			nd.setId(IdFactory.getDateTimeRandomStr());
			nd.setIp(invoker.getIp());
			nd.setPort(String.valueOf(invoker.getPort()));
			data.getNodes().add(nd);
			
		}else if (invoker instanceof Provider){
			nd.setPort(String.valueOf(invoker.getPort()));
		}
		nd.addInvoker(invoker);
		return nd;
	}
	private void searchAndAddLineDomain(String sourceId,String targetId,String serivices){
		LineDomain ld = data.searchLineDomain(sourceId, targetId);
		if(ld == null){
			ld = new LineDomain();
			ld.setSource(sourceId);
			ld.setTarget(targetId);
			data.getLines().add(ld);
		}
		
		ld.addServices(serivices);
	}
	
	private List<Invoker> searchProvicer(Invoker invoker){
		List<Invoker> result = Lists.newArrayList();
		for(Invoker p:providers){
			if(p.getServiceName().equals(invoker.getServiceName()) && Tool.isMatchGroup(invoker.getUrl(), p.getUrl())){
				result.add(p);
			}
		}
		return result;
	}
	
	private List<Invoker> searchConsumers(Invoker invoker){
		List<Invoker> result = Lists.newArrayList();
		for(Invoker p:consumer){
			if(p.getServiceName().equals(invoker.getServiceName()) && Tool.isMatchGroup( p.getUrl(),invoker.getUrl())){
				result.add(p);
			}
		}
		return result;
	}
	
	public InvokerRealationDomain getReleationData(){
		InvokerRealationDomain d = new InvokerRealationDomain();
		d.setLines(Lists.newLinkedList(data.getLines()));
		d.setNodes(Lists.newLinkedList(data.getNodes()));
		return d;
	}
	
	public Set<? extends Invoker> findInvokers(String key){
		if(Constants.PROVIDERS_CATEGORY.equalsIgnoreCase(key)){
			return Sets.newHashSet(this.providers);
		}else if (Constants.CONSUMERS_CATEGORY.equalsIgnoreCase(key)){
			return Sets.newHashSet(this.consumer);
		}
		return Sets.newHashSet();
	}

}
