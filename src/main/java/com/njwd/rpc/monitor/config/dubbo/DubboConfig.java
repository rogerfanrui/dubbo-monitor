package com.njwd.rpc.monitor.config.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.registry.RegistryService;

@Configuration
@EnableConfigurationProperties(DubboProperties.class)
public class DubboConfig {

	@Autowired
	private DubboProperties properties; 

	@Bean
	@ConditionalOnMissingBean
	public ApplicationConfig applicationConfig() {
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setName(properties.getApplicationName());
		applicationConfig.setLogger("slf4j");
		return applicationConfig;
	}

	@Bean
	@ConditionalOnMissingBean(RegistryConfig.class)
	public RegistryConfig registryConfig() {
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress(properties.getRegistryAddress());
		registryConfig.setCheck(properties.isRegistryCheck());
		registryConfig.setTimeout(properties.getRegistryTimeout());
		return registryConfig;
	}

	

	@Bean
	@ConditionalOnMissingBean(ProtocolConfig.class)
	public ProtocolConfig protocolConig() {
		ProtocolConfig protocol = new ProtocolConfig();
		protocol.setName(properties.getProtocolName());
		protocol.setPort(properties.getProtocolPort());
		return protocol;
	}

	@Bean
	@ConditionalOnMissingBean(RegistryService.class)
	public RegistryService registerServices(ApplicationConfig appConfig,
			RegistryConfig config) {

		ReferenceConfig<RegistryService> reference = new ReferenceConfig<RegistryService>();
		reference.setApplication(appConfig);
		reference.setRegistry(config);
		reference.setInterface(RegistryService.class);
		return reference.get();
	}

}
