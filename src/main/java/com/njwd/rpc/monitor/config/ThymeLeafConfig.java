package com.njwd.rpc.monitor.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;

@Configuration
@AutoConfigureBefore(ThymeleafAutoConfiguration.class)
public class ThymeLeafConfig {

	
	@Bean
	@ConditionalOnMissingBean
	public DataAttributeDialect dataAttributeDialect() {
		return new DataAttributeDialect();
	} 
}
