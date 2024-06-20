package com.example.token.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Bean
    public RateLimiter rateLimiter() {
        // Allow 1 request per second
        return RateLimiter.create(1.0);
    }
}
