package com.cpu.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
@ConditionalOnBean(RedisConnectionFactory.class)
public class RedisConfig {

    // RedisConnectionFactory is auto-configured by Spring Boot
    // from spring.data.redis.* properties — no manual bean needed

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        log.info("Initializing Redis Cache Manager...");
        
        // Retry logic - Redis might be starting
        int maxRetries = 3;
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                connectionFactory.getConnection().ping();
                log.info("✓ Redis connection successful on attempt {}", retryCount + 1);
                
                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))
                        .computePrefixWith(cacheName -> "cache:")
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

                return RedisCacheManager.builder(connectionFactory)
                        .cacheDefaults(config)
                        .build();
            } catch (Exception e) {
                retryCount++;
                if (retryCount < maxRetries) {
                    log.warn("⚠ Redis connection failed (attempt {}/{}), retrying in 2 seconds...", retryCount, maxRetries);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    log.warn("⚠ Redis connection failed after {} attempts, falling back to in-memory cache", maxRetries);
                    log.info("Using ConcurrentMapCacheManager (in-memory) as fallback");
                    
                    // Fallback to in-memory cache
                    return new ConcurrentMapCacheManager(
                            "cpus", 
                            "allCpus", 
                            "manufacturers", 
                            "allManufacturers", 
                            "technologies", 
                            "allTechnologies",
                            "specifications",
                            "allSpecifications",
                            "benchmarks",
                            "allBenchmarks",
                            "manufacturerStats",
                            "benchmarkStats",
                            "benchmarkStatsByCpu",
                            "cpuRanking"
                    );
                }
            }
        }
        
        return new ConcurrentMapCacheManager("default");
    }
}

