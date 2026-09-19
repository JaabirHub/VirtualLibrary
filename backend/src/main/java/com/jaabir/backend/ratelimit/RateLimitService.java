package com.jaabir.backend.ratelimit;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;

@Service
public class RateLimitService {

    private final LettuceBasedProxyManager proxyManager;

    public RateLimitService(LettuceBasedProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    public boolean allowSearchRequest(String key) {
        // 30 requests per minute
      Refill refill = Refill.intervally(30, Duration.ofMinutes(1));
      Bandwidth limit = Bandwidth.classic(30, refill);

      BucketConfiguration configuration = BucketConfiguration.builder()
              .addLimit(limit)
              .build();

      Bucket bucket = proxyManager.builder()
              .build(key.getBytes(StandardCharsets.UTF_8), () -> configuration);

      return bucket.tryConsume(1);
    }

    public boolean allowLoginRequest(String key) {
      Refill refill = Refill.intervally(5, Duration.ofMinutes(1));
      Bandwidth limit = Bandwidth.classic(5, refill);

      BucketConfiguration configuration = BucketConfiguration.builder()
              .addLimit(limit)
              .build();

      Bucket bucket = proxyManager.builder()
              .build(key.getBytes(StandardCharsets.UTF_8), () -> configuration);

      return bucket.tryConsume(1);
    }

    public boolean allowRegisterRequest(String key) {
      Refill refill = Refill.intervally(5, Duration.ofMinutes(1));
      Bandwidth limit = Bandwidth.classic(5, refill);

      BucketConfiguration configuration = BucketConfiguration.builder()
              .addLimit(limit)
              .build();

      Bucket bucket = proxyManager.builder()
              .build(key.getBytes(StandardCharsets.UTF_8), () -> configuration);

      return bucket.tryConsume(1);
    }
}