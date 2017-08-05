package com.njwd.rpc.monitor.web.controller.alarm;

import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.ModelAndView;

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
	public ModelAndView addrule(@RequestParam(required=false) String id,ModelAndView view){
		view.setViewName("alarm/addrule");
		AlarmRule rule = new AlarmRule();
		if(id !=null){
			List<AlarmRule> rules =this.alarmRuleServices.rules(id);
			if(rules !=null && !rules.isEmpty()){
				rule=rules.get(0);
			}
		}
		view.addObject("rule",rule );
		return view;
	}
	@RequestMapping(value="/loadRule",method=RequestMethod.GET)
	@ResponseBody
	public R<List<AlarmRule>> loadRule(@ApiParam(required=false,value="规则ID,如果为空则查询所有")@RequestParam(required=false) String id){
		List<AlarmRule> rules =alarmRuleServices.rules(id);
		return R.ok(rules);
	}
	
	@RequestMapping(value="/do/addRule",method=RequestMethod.POST)
	@ResponseBody
	public R<Boolean> doAddRule(@Valid AlarmRule rule){
		alarmRuleServices.addOrUpdateRule(rule);
		return R.ok(true);
	}
	
	@RequestMapping(value="/do/deleteRule",method=RequestMethod.POST)
	@ResponseBody
	public R<Boolean> deleteRule( AlarmRule rule){
		alarmRuleServices.deleteRule(rule);
		return R.ok(true);
	}
}
