package com.jaabir.backend.config;

import java.time.Duration;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.jaabir.backend.googlebooks.GoogleBooksResponse;
import com.jaabir.backend.googlebooks.VolumeItem;

import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper) {

        RedisSerializationContext.SerializationPair<String> keySerializer =
                RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer());

        JacksonJsonRedisSerializer<GoogleBooksResponse>
                googleBooksResponseSerializer =
                new JacksonJsonRedisSerializer<>(
                        objectMapper,
                        GoogleBooksResponse.class
                );

        JacksonJsonRedisSerializer<VolumeItem>
                volumeItemSerializer =
                new JacksonJsonRedisSerializer<>(
                        objectMapper,
                        VolumeItem.class
                );

        RedisCacheConfiguration searchCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))
                        .disableCachingNullValues()
                        .serializeKeysWith(keySerializer)
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(
                                                googleBooksResponseSerializer
                                        )
                        );

        RedisCacheConfiguration bookCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))
                        .disableCachingNullValues()
                        .serializeKeysWith(keySerializer)
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(volumeItemSerializer)
                        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(searchCacheConfiguration)
                .withInitialCacheConfigurations(
                        Map.of(
                                "googleBooksSearch",
                                searchCacheConfiguration,
                                "googleBookById",
                                bookCacheConfiguration
                        )
                )
                .build();
    }
}