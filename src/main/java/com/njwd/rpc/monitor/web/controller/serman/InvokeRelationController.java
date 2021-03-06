package com.njwd.rpc.monitor.web.controller.serman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.njwd.rpc.monitor.core.domain.inrelation.InvokerRealationDomain;
import com.njwd.rpc.monitor.core.meta.InvokerRealtionServices;
import com.njwd.rpc.monitor.web.R;

@Controller
@RequestMapping("/relat")
public class InvokeRelationController {

	@Autowired
	InvokerRealtionServices  invokerRealtionServices;
	
	@RequestMapping("/index")
	public ModelAndView index(ModelAndView view){
		view.setViewName("invokeman/relation");
		return view; 
	}
	
	@RequestMapping(value="/datas",method = RequestMethod.GET)
	@ResponseBody
	public R<InvokerRealationDomain> getDatas(){
		return R.ok(invokerRealtionServices.getReleationData());
	}
	
	
}
