package com.njwd.rpc.monitor.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Configuration
@EnableCaching
public class StringRedisConfig extends CachingConfigurerSupport {

	@Bean
	public CacheManager cacheManager(
			@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		RedisCacheManager redisCacheManager = new RedisCacheManager(
				redisTemplate);
		return redisCacheManager;
	}

	@Bean
	public StringRedisTemplate template(RedisConnectionFactory factory) {
		Jackson2JsonRedisSerializer serializer=getJacksonRedisSeri();
		StringRedisTemplate template = new StringRedisTemplate(factory);
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);
		template.afterPropertiesSet();

		return template;
	}

	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory rcf) {

		Jackson2JsonRedisSerializer serializer=getJacksonRedisSeri();

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(rcf);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	private Jackson2JsonRedisSerializer getJacksonRedisSeri() {
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL,
				com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		om.registerModule(new JodaModule());
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(
				Object.class);
		serializer.setObjectMapper(om);
		return serializer;
	}

}
