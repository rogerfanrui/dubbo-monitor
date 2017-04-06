package com.njwd.rpc.monitor.core.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class Consumer extends Service {

	private List<Method> methods;

	@JsonIgnore
	private URL url;

	
	
	public Consumer() {
	}

	public Consumer(URL url) {
		super(url);
		this.url = url;
		String _methods = url.getParameter(Constants.METHODS_KEY);
		if (StringUtils.isNotBlank(_methods)) {
			List<Method> _methodsList = Lists.transform(Lists.newArrayList(_methods.split(",")),
					new Function<String, Method>() {
						public Method apply(String input) {
							return new Method(input);
						}
					});
			this.methods = Lists.newArrayList(_methodsList);
		}
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	public boolean equals(Object obj) {
		Consumer p = (Consumer) obj;
		return url.equals(p.getUrl());
	}
	
	

	
	public int hashCode() {

		return url.hashCode();
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

}
