package com.example.hazelcastdemo.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfiguration implements CachingConfigurer {
    @Bean
    public CacheManager cacheManager() {
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().addAddress("127.0.0.1:5701");
        HazelcastInstance hInstance = HazelcastClient.newHazelcastClient(config);
        return new HazelcastCacheManager(hInstance);
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}
