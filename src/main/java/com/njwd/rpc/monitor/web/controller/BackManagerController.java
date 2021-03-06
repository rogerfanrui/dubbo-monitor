package com.njwd.rpc.monitor.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.URL;
import com.njwd.rpc.monitor.config.SpringUtils;
import com.njwd.rpc.monitor.core.alarm.AlarmRuleServices;
import com.njwd.rpc.monitor.core.meta.InvokeMetaEvent;
import com.njwd.rpc.monitor.web.R;

@RestController
@RequestMapping(value = "/m")
@Api(value = "back-controller")
public class BackManagerController {

	@Autowired
	AlarmRuleServices alarmRuleServices;
	
	@RequestMapping(value = "/addurl", method = RequestMethod.POST)
	@ApiOperation(value = "增加URL", httpMethod = "POST")
	public R<Boolean> addUrl(@RequestParam @ApiParam(value = "url", required = true) String url){
		InvokeMetaEvent event = new InvokeMetaEvent(this, URL.valueOf(url));
		SpringUtils.getApplicationContext().publishEvent(event);
		return R.ok(true);
		
	}
	
	@RequestMapping(value = "/reload/alarmCache", method = RequestMethod.GET)
	@ApiOperation(value = "重新加载alarmCache", httpMethod = "GET")
	public R<Boolean> reloadAlarmCache(){
		alarmRuleServices.load();
		return R.ok(true);
		
	}
}
