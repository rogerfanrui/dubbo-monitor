package com.njwd.rpc.monitor.core.domain;

import java.util.Map;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;

public class Oride extends Invoker {

	private String params;

	private String username;

	private boolean enabled;

	

	public Oride() {
	}
	public Oride(URL url) {
		super(url);
		this.params = url.getParameter("mock","");
	}
	

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, String> toParametersMap() {
		Map<String, String> map = StringUtils.parseQueryString(getParams());
		map.remove(Constants.INTERFACE_KEY);
		map.remove(Constants.GROUP_KEY);
		map.remove(Constants.VERSION_KEY);
		map.remove(Constants.APPLICATION_KEY);
		map.remove(Constants.CATEGORY_KEY);
		map.remove(Constants.DYNAMIC_KEY);
		map.remove(Constants.ENABLED_KEY);
		return map;
	}

	public URL toUrl() {

		StringBuilder sb = new StringBuilder();
		sb.append(Constants.OVERRIDE_PROTOCOL);
		sb.append("://");
		 String address=this.getIp();
		if (!StringUtils.isBlank(address)
				&& !Constants.ANY_VALUE.equals(address)) {
			sb.append(address);
		} else {
			sb.append(Constants.ANYHOST_VALUE);
		}
		sb.append("/");
		sb.append(this.getServiceName());
		sb.append("?");
		Map<String, String> param = StringUtils.parseQueryString(params);
		param.put(Constants.CATEGORY_KEY, Constants.CONFIGURATORS_CATEGORY);
		param.put(Constants.ENABLED_KEY, String.valueOf(isEnabled()));
		param.put(Constants.DYNAMIC_KEY, "false");
		String application = this.getAppName();
		if (!StringUtils.isBlank(application)
				&& !Constants.ANY_VALUE.equals(application)) {
			param.put(Constants.APPLICATION_KEY, application);
		}
		String group= this.getGroup();
		if (group != null) {
			param.put(Constants.GROUP_KEY, group);
		}

		sb.append(StringUtils.toQueryString(param));
		return URL.valueOf(sb.toString());
	}

}
