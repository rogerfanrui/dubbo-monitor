package com.njwd.rpc.monitor.config.dubbo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.AnnotationBean;

/**
 * 单独在这里定义的原因是 
 *AnnotationBean 实现了 BeanFactoryPostProcessor
 *所以一开始的时候就会初始化这个类 导致DubboConfig 配置无效
 * 
 */
@Configuration
@ConditionalOnClass(DubboProperties.class)
public class DubboAnnotationConfig {

	@Bean
	@ConditionalOnMissingBean
	public AnnotationBean annotationBean(
			@Value("${dubbo.annotationPackage}") String packageName) {
		AnnotationBean annotationBean = new AnnotationBean();
		annotationBean.setPackage(packageName);
		return annotationBean;
	}
}
