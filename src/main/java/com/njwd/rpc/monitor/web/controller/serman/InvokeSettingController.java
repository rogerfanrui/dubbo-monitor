package com.njwd.rpc.monitor.web.controller.serman;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.meta.InvokerRealtionServices;
import com.njwd.rpc.monitor.web.R;

@Controller
@RequestMapping("/isett")
public class InvokeSettingController {

	@Autowired
	InvokerRealtionServices  invokerRealtionServices;
	
	@RequestMapping("/index")
	public String index(){
		return "invokeman/isett";
	}
	
	@RequestMapping("/info")
	@ResponseBody
	public R<Set<Invoker>> all(@RequestParam String key){
		Set<Invoker> result =	(Set<Invoker> )invokerRealtionServices.findInvokers(key);
		
		return R.getSuccessResponse(result);
	}
}
