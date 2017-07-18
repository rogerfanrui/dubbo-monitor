package com.njwd.rpc.monitor.core.domain;

import com.alibaba.dubbo.common.URL;

public class Route extends Invoker {

	private Boolean dynamic;
	private Boolean runtime;
	private String router;
	private String name;

	public Route(){
		
	}
	public Route(URL url) {
		super(url);
		this.dynamic = url.getParameter("dynamic", false);
		this.runtime = url.getParameter("runtime", false);
		this.router = url.getParameter("router");
		this.name = url.getParameter("name");
		
	}

	public Boolean getDynamic() {
		return dynamic;
	}

	public void setDynamic(Boolean dynamic) {
		this.dynamic = dynamic;
	}

	public Boolean getRuntime() {
		return runtime;
	}

	public void setRuntime(Boolean runtime) {
		this.runtime = runtime;
	}

	public String getRouter() {
		return router;
	}

	public void setRouter(String router) {
		this.router = router;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public URL toUrl() {
		// String group = null;
		// String version = null;
		// String path = service;
		// int i = path.indexOf("/");
		// if (i > 0) {
		// group = path.substring(0, i);
		// path = path.substring(i + 1);
		// }
		// i = path.lastIndexOf(":");
		// if (i > 0) {
		// version = path.substring(i + 1);
		// path = path.substring(0, i);
		// }
		// return URL
		// .valueOf(Constants.ROUTE_PROTOCOL
		// + "://"
		// + Constants.ANYHOST_VALUE
		// + "/"
		// + path
		// + "?"
		// + Constants.CATEGORY_KEY
		// + "="
		// + Constants.ROUTERS_CATEGORY
		// + "&router=condition&runtime=false&enabled="
		// + isEnabled()
		// + "&priority="
		// + getPriority()
		// + "&force="
		// + isForce()
		// + "&dynamic=false"
		// + "&name="
		// + getName()
		// + "&"
		// + Constants.RULE_KEY
		// + "="
		// + URL.encode(getMatchRule() + " => " + getFilterRule())
		// + (group == null ? "" : "&" + Constants.GROUP_KEY + "="
		// + group)
		// + (version == null ? "" : "&" + Constants.VERSION_KEY
		// + "=" + version));

		return null;
	}

}
