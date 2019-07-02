package com.mycompany.myapp.config;

import com.mycompany.myapp.repository.UserRepository;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SslProvider;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://localhost:6379")
            .setSubscriptionConnectionMinimumIdleSize(1)
            .setSubscriptionConnectionPoolSize(50)
            .setConnectionMinimumIdleSize(24)
            .setConnectionPoolSize(64)
            .setDnsMonitoringInterval(5000)
            .setIdleConnectionTimeout(10000)
            .setConnectTimeout(10000)
            .setTimeout(3000)
            .setRetryAttempts(3)
            .setRetryInterval(1500)
            .setDatabase(0)
            .setPassword(null)
            .setSubscriptionsPerConnection(5)
            .setClientName(null)
            .setSslEnableEndpointIdentification(true)
            .setSslProvider(SslProvider.JDK)
            .setSslTruststore(null)
            .setSslTruststorePassword(null)
            .setSslKeystore(null)
            .setSslKeystorePassword(null)
            .setPingConnectionInterval(0)
            .setKeepAlive(false)
            .setTcpNoDelay(false);
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();

        config.put(UserRepository.USERS_BY_LOGIN_CACHE, new CacheConfig());
        config.put(UserRepository.USERS_BY_EMAIL_CACHE, new CacheConfig());
        return new RedissonSpringCacheManager(redissonClient, config);
    }


}
