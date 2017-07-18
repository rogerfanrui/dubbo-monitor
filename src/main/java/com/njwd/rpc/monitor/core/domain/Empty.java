package com.njwd.rpc.monitor.core.domain;

import com.alibaba.dubbo.common.URL;

public class Empty extends Invoker {

	private String category;

	public Empty() {

	}

	public Empty(URL url) {
		super(url);
		this.category = url.getParameter("category");
		
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
