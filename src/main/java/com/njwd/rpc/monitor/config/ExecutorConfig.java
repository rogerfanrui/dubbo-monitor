
package com.njwd.rpc.monitor.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync  
public class ExecutorConfig {

    private int corePoolSize = 2;  
    private int maxPoolSize = 5;  
    private int queueCapacity = 10000;  
  
    @Bean
    public Executor mySimpleAsync() {  
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        executor.setCorePoolSize(corePoolSize);  
        executor.setMaxPoolSize(maxPoolSize);  
        executor.setQueueCapacity(queueCapacity);  
        executor.setThreadNamePrefix("monitor-Queue-");  
        executor.initialize();  //hi
        return executor;  
    }  
}
