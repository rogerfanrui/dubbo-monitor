package com.njwd.rpc.monitor.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.njwd.rpc.monitor.core.domain.Consumer;
import com.njwd.rpc.monitor.core.domain.Provider;
import com.njwd.rpc.monitor.core.services.ConsumerService;
import com.njwd.rpc.monitor.core.services.MockServices;
import com.njwd.rpc.monitor.core.services.ProviderService;
import com.njwd.rpc.monitor.core.services.api.ConsumerServicesManager;
import com.njwd.rpc.monitor.core.services.api.DubboCoreServicesHandler;
import com.njwd.rpc.monitor.core.services.api.ProviderServicesManager;

@RestController
public class MonitorController {
	
	@Resource(name="ConsumerService")
	DubboCoreServicesHandler<Consumer> conserives;
	@Resource(name="ProviderService")
	DubboCoreServicesHandler<Provider> proservices;
	@Resource(name="ConsumerService")
	ConsumerServicesManager consumerManager;
	
	@Resource(name="ProviderService")
	ProviderServicesManager providerManager;
	
	@RequestMapping(value = "/consumer", method = RequestMethod.GET)
	public ResponseVo<List<Consumer>> queryConsumers(){
	  return ResponseVo.getSuccessResponse(	conserives.findAll());
	}
	
	@RequestMapping(value = "/provider", method = RequestMethod.GET)
	public ResponseVo<List<Provider>> queryProviders(){ 
	  return ResponseVo.getSuccessResponse(	proservices.findAll());
	}
	
	@RequestMapping(value = "/consumer/recoveMock", method = RequestMethod.POST)
	public ResponseVo<Boolean> recoveMock(@RequestParam String services,@RequestParam  String appName,@RequestParam  String ip){
		consumerManager.mock(services, appName, ip, null);
		
		return ResponseVo.getSuccessResponse(true);
	}
	@RequestMapping(value = "/consumer/doMock", method = RequestMethod.POST)
	public ResponseVo<Boolean> doMock(@RequestParam String services,@RequestParam  String appName,@RequestParam  String ip){
		consumerManager.mock(services, appName, ip, MockServices.MOCK_UPDOWN_VALUE);
		return ResponseVo.getSuccessResponse(true);
	}
	@RequestMapping(value = "/provider/recoveMock", method = RequestMethod.POST)
	public ResponseVo<Boolean> recoveProviderMock(@RequestParam String services,@RequestParam  String groupName){
		providerManager.mock(services, groupName, null);
		
		return ResponseVo.getSuccessResponse(true);
	}
	@RequestMapping(value = "/provider/doMock", method = RequestMethod.POST)
	public ResponseVo<Boolean> doMockProvider(@RequestParam String services,@RequestParam  String groupName){
		providerManager.mock(services, groupName, MockServices.MOCK_UPDOWN_VALUE);
		return ResponseVo.getSuccessResponse(true);
	}
}
