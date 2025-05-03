package com.infitry.rediscluster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20000);
        executor.setMaxPoolSize(20000);
        executor.setQueueCapacity(20000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("Async-");
        executor.setVirtualThreads(true);
        executor.initialize();
        return executor;
    }
}
