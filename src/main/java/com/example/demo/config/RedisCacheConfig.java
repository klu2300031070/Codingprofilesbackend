package com.example.demo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
@EnableCaching 
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Compute exactly how much time is left from right now until 11:59:59 PM tonight
        Duration timeUntilMidnight = calculateTimeUntilMidnight();

        System.out.println("⏱️ [REDIS INIT] - Cache TTL set dynamically until Midnight Tonight: " 
                + timeUntilMidnight.toHours() + " hours.");

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeUntilMidnight) // Sets the dynamic expiration to midnight tonight
                .disableCachingNullValues()   
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer())) 
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer())); // Handles Lists seamlessly!

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    private Duration calculateTimeUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnightTonight = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
        return Duration.between(now, midnightTonight);
    }
}