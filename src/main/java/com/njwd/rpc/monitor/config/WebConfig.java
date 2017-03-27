package com.njwd.rpc.monitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	 @Override 
	 public void addViewControllers(ViewControllerRegistry registry) { 
	 registry.addViewController( "/" ).setViewName( "redirect:/index.html" );
	 registry.addViewController("/error").setViewName("error.html"); 
	 registry.setOrder(Ordered.HIGHEST_PRECEDENCE); 
	 }
	@Override  
    public void addResourceHandlers(ResourceHandlerRegistry registry) {  
  
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");  
        super.addResourceHandlers(registry);  
    }  
}
