package com.njwd.rpc.monitor.core.domain.inrelation;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.util.Tool;

public class InvokerRealationDomain {

	
	
	private List<NodeDomain> nodes = Lists.newLinkedList();
	private List<LineDomain> lines= Lists.newLinkedList();
	
	
	public  NodeDomain searchNodeDomain(String appName){
		for(NodeDomain d : nodes){
			if(d.getAppName().equalsIgnoreCase(appName)){
				return d;
			}
		}
		return null;
	}
	
	
	public LineDomain  searchLineDomain(String source,String target){
		for(LineDomain ld:lines){
			if((source==null||ld.getSource().equals(source))&& (target==null||ld.getTarget().equals(target))){
				return ld;
			}
		}
		return null;
	}
	public List<LineDomain>  searchLineDomainList(String source,String target){
		List<LineDomain> result = Lists.newLinkedList();
		for(LineDomain ld:lines){
			if((source==null||ld.getSource().equals(source))&& (target==null||ld.getTarget().equals(target))){
				result.add(ld);
			}
		}
		return result;
	}
	

	public List<LineDomain> getLines() {
		return lines;
	}

	public void setLines(List<LineDomain> lines) {
		this.lines = lines;
	}

	public List<NodeDomain> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeDomain> nodes) {
		this.nodes = nodes;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
