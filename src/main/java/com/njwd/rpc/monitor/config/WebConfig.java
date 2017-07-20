package com.njwd.rpc.monitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Value("sweager.enable")
	private boolean sweagerEnabel=false;
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:/index.html");
		registry.addViewController("/error").setViewName("error");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/static/**").addResourceLocations(
				ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
		
		if(sweagerEnabel){
			registry.addResourceHandler("swagger-ui.html").addResourceLocations(
					"classpath:/META-INF/resources/");

			registry.addResourceHandler("/webjars/**").addResourceLocations(
					"classpath:/META-INF/resources/webjars/");
		}
		

		super.addResourceHandlers(registry);
	}
}
