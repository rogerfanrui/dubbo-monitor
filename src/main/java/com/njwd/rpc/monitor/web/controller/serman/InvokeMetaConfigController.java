package com.njwd.rpc.monitor.web.controller.serman;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.google.common.collect.Maps;
import com.njwd.rpc.monitor.core.domain.Invoker;
import com.njwd.rpc.monitor.core.meta.InvokerConfigServices;
import com.njwd.rpc.monitor.web.R;

@Controller
@RequestMapping("/meta")
@Api(value = "服务配置管理")
public class InvokeMetaConfigController {

	@Autowired
	InvokerConfigServices invokerConfigServices;
	
	@ApiIgnore
	@RequestMapping("/index")
	public String index(){
		return "invokeman/metaindex";
	}
	
	@ApiOperation(value="获取服务配置列表", notes="")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	@ResponseBody
	public R<Map<String, Map<String, List<Invoker>>>> all(){
		Map<String, Map<String, List<Invoker>>> result =	invokerConfigServices.selectAll();
		if(result == null){
			result = Maps.newHashMap();
		}
		return R.getSuccessResponse(result);
	}
}
