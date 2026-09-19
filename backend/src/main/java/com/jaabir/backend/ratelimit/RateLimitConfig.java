package com.jaabir.backend.ratelimit;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;

@Configuration
public class RateLimitConfig {

    @Bean
    public LettuceBasedProxyManager bucketProxyManager(
            LettuceConnectionFactory connectionFactory
    ) {
        String uri = "redis://" +
                connectionFactory.getHostName() + ":" +
                connectionFactory.getPort();

        RedisClient redisClient = RedisClient.create(uri);

        RedisCodec<byte[], byte[]> codec = RedisCodec.of(
                ByteArrayCodec.INSTANCE,
                ByteArrayCodec.INSTANCE
        );

        StatefulRedisConnection<byte[], byte[]> connection =
                redisClient.connect(codec);

        ExpirationAfterWriteStrategy expirationStrategy =
                ExpirationAfterWriteStrategy
                        .basedOnTimeForRefillingBucketUpToMax(
                                Duration.ofMinutes(10)
                        );

        return LettuceBasedProxyManager.builderFor(connection)
                .withExpirationStrategy(expirationStrategy)
                .build();
    }
}