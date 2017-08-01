package com.njwd.rpc.monitor.web.controller.serman;

import io.swagger.annotations.ApiOperation;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.dubbo.DubboMockServices;
import com.njwd.rpc.monitor.core.meta.InvokerRealtionServices;
import com.njwd.rpc.monitor.web.R;

@Controller
@RequestMapping("/isett")
public class InvokeSettingController {

	@Autowired
	InvokerRealtionServices  invokerRealtionServices;

	@Autowired
	private DubboMockServices mockServices;
	@RequestMapping("/index")
	public String index(){
		return "invokeman/isett";
	}
	
	@RequestMapping(value ="/info",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询配置信息", httpMethod = "GET")
	public R<Set<Invoker>> all(@RequestParam String key){
		Set<Invoker> result =	(Set<Invoker> )invokerRealtionServices.findInvokers(key);
		
		return R.ok(result);
	}
	
	
	@RequestMapping(value ="/mock",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "降级", httpMethod = "POST")
	public R<Boolean> mock(@Valid Invoker invoker){
		mockServices.executeMock(invoker, true);
		
		return R.ok();
	}
	
	@RequestMapping(value ="/unmock",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "取消降级", httpMethod = "POST")
	public R<Boolean> unmock(@Valid Invoker invoker){
		mockServices.executeMock(invoker, false);
		
		return R.ok();
	}
}
