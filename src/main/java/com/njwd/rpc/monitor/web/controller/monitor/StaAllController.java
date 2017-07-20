package com.njwd.rpc.monitor.web.controller.monitor;

import java.util.Collection;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.njwd.rpc.monitor.core.monitor.StaAllServices;
import com.njwd.rpc.monitor.core.monitor.domain.StaAll;
import com.njwd.rpc.monitor.web.R;


@Controller
@RequestMapping("/sta/all")
public class StaAllController {

	@Autowired
	StaAllServices staAllServices;
	@RequestMapping("/index")
	public ModelAndView index(ModelAndView view){
		Collection<String> sets =staAllServices.getStaServices();
		if(sets == null){
			sets = Sets.newHashSet();
		}else{
			sets=	Collections2.transform(sets, new Function<String, String>() {

				@Override
				public String apply(String input) {
					return input.split("_")[1];
				}
				
			});
		}
		view.addObject("services", sets);
		view.setViewName("sta/all");;
		return view;
	}
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public R<Set<StaAll>> lists(@RequestBody Set<String> keys){
		return R.ok(staAllServices.selectInfosByKeys(keys )); 
	}
}
