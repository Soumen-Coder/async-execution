package com.springframework.asyncexecution.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Configuration
//@EnableAsync
public class AsyncConfiguration {
    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        return CompletableFuture.delayedExecutor(0, TimeUnit.SECONDS);
    }
}
