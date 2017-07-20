package com.njwd.rpc.monitor.web.controller.monitor;

import java.util.Collection;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.njwd.rpc.monitor.core.monitor.StaAllServices;


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
	
}
