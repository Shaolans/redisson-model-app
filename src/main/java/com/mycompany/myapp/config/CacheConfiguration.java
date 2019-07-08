package com.mycompany.myapp.config;

import io.github.jhipster.config.JHipsterProperties;
import org.hibernate.cache.jcache.ConfigSettings;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.redisson.config.SslProvider;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;


@Configuration
@EnableCaching
public class CacheConfiguration {
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        MutableConfiguration<Object, Object> jcacheConfig = new MutableConfiguration<>();
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
        jcacheConfig.setStatisticsEnabled(true);
        jcacheConfiguration = RedissonConfiguration.fromInstance(Redisson.create(config), jcacheConfig);
    }


    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cm) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cm);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.Project.class.getName());
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
        cm.enableStatistics(cacheName, true);
    }


}
