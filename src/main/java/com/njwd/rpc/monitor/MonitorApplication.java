package com.njwd.rpc.monitor;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.Banner.Mode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({ "com.njwd.rpc.monitor.**" })
@EnableAutoConfiguration
@EnableWebMvc
public class MonitorApplication {

	

	//main
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MonitorApplication.class);
		app.setBannerMode(Mode.OFF);
		app.run(args);
	}
}
