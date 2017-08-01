package com.njwd.rpc.monitor.web.controller.alarm;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.njwd.rpc.monitor.core.alarm.AlarmRuleServices;
import com.njwd.rpc.monitor.core.domain.alarm.AlarmRule;
import com.njwd.rpc.monitor.web.R;

@Controller
@RequestMapping("/alarm")
public class AlarmRuleController {
	
	@Autowired
	AlarmRuleServices alarmRuleServices;
	
	@RequestMapping("/index")
	public String index(){
		return "alarm/index";
	}
	@RequestMapping("/addrule")
	public String addrule(){
		return "alarm/addrule";
	}
	@RequestMapping(value="/loadRule",method=RequestMethod.GET)
	@ResponseBody
	public R<List<AlarmRule>> loadRule(){
		List<AlarmRule> rules =alarmRuleServices.rules();
		return R.ok(rules);
	}
	
	@RequestMapping(value="/do/addRule",method=RequestMethod.POST)
	@ResponseBody
	public R<Boolean> doAddRule(@Valid AlarmRule rule){
		alarmRuleServices.addOrUpdateRule(rule);
		return R.ok(true);
	}
}
