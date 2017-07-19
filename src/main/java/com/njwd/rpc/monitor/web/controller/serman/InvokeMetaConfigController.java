package com.njwd.rpc.monitor.web.controller.serman;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.meta.InvokerConfigServices;
import com.njwd.rpc.monitor.web.R;

@Controller
@RequestMapping("/meta")
public class InvokeMetaConfigController {

	@Autowired
	InvokerConfigServices invokerConfigServices;
	@RequestMapping("/index")
	public String index(){
		return "invokeman/metaindex";
	}
	@RequestMapping("/all")
	@ResponseBody
	public R<Map<String, Map<String, List<Invoker>>>> all(){
		Map<String, Map<String, List<Invoker>>> result =	invokerConfigServices.selectAll();
		if(result == null){
			result = Maps.newHashMap();
		}
		return R.getSuccessResponse(result);
	}
}
