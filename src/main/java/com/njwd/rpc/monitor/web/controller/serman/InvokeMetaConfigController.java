package com.njwd.rpc.monitor.web.controller.serman;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.core.domain.ConditionRoute;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.dubbo.DubboConditionRouteServices;
import com.njwd.rpc.monitor.core.meta.InvokerConfigServices;
import com.njwd.rpc.monitor.web.R;

@Controller
@RequestMapping("/meta")
@Api(value = "服务配置管理")
public class InvokeMetaConfigController {

	@Autowired
	InvokerConfigServices invokerConfigServices;
	
	@Autowired
	DubboConditionRouteServices dubboConditionRouteServices;
	@ApiIgnore
	@RequestMapping("/index")
	public String index(){
		return "invokeman/metaindex";
	}
	@ApiIgnore
	@RequestMapping("/addroute")
	public String addroute(){
		return "invokeman/addroute";
	}
	@ApiOperation(value="获取服务配置列表")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	@ResponseBody
	public R<Map<String, Map<String, List<Invoker>>>> all(){
		Map<String, Map<String, List<Invoker>>> result =	invokerConfigServices.selectAll();
		if(result == null){
			result = Maps.newHashMap();
		}
		return R.ok(result);
	}
	
	
	@ApiOperation(value="增加动态路由")
	@RequestMapping(value="/addRule",method=RequestMethod.POST)
	@ResponseBody
	public R<Boolean> addRule(@Valid ConditionRoute route){
		dubboConditionRouteServices.addConditionRule(route);
		return R.ok(true);
	}
	
	@ApiOperation(value="删除动态路由")
	@RequestMapping(value="/removeRule",method=RequestMethod.POST)
	@ResponseBody
	public R<Boolean> removeRule(@RequestParam String url){
		dubboConditionRouteServices.removeCondition(url);
		return R.ok(true);
	}
	
	
	
}
