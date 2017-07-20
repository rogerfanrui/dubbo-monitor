package com.njwd.rpc.monitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket petApi(@Value("${sweager.enable}") boolean sweagerEnabel) {
		if(sweagerEnabel){
			return  new Docket(DocumentationType.SWAGGER_2)
	        .apiInfo(apiInfo())
	        .select()
	        .apis(RequestHandlerSelectors.basePackage("com.njwd.rpc.monitor.web.controller"))
	        .paths(PathSelectors.any())
	        .build();
		}else{
			return  new Docket(DocumentationType.SWAGGER_2);
		}
		


	}

	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("njwd 服务管理平台")
                .description("njwd 服务管理平台")
                .version("1.0")
                .build();
    }


}
