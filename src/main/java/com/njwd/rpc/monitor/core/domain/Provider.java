package com.njwd.rpc.monitor.core.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.njwd.rpc.monitor.core.util.ConvertUtil;

public class Provider extends Invoker {

	

	private List<Method> methods;

	public Provider() {
	}

	public Provider(URL url) {
		super(url);
		String _methods = url.getParameter(Constants.METHODS_KEY);
		if (StringUtils.isNotBlank(_methods)) {
			List<Method> _methodsList = Lists.transform(
					Lists.newArrayList(_methods.split(",")),
					new Function<String, Method>() {
						public Method apply(String input) {
							return new Method(input);
						}
					});

			this.methods = Lists.newArrayList(_methodsList);
		}

	}

	public boolean equals(Object obj) {
		Provider p = (Provider) obj;
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



	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

}
