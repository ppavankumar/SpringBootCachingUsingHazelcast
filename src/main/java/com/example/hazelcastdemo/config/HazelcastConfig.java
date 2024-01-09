package com.example.hazelcastdemo.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class HazelcastConfig {
    @Bean
    public Config config() {
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1:5701");
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(final Config config) {
        return Hazelcast.getOrCreateHazelcastInstance(config.setInstanceName("studentConfig"));
    }
}
