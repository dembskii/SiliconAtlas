package com.cpu.management.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Value("${rate.limit.requests:100}")
    private long requests;

    @Value("${rate.limit.duration:1}")
    private long duration;

    @Value("${rate.limit.key-prefix:ratelimit:}")
    private String keyPrefix;

    public boolean allowRequest(String identifier) {
        String key = keyPrefix + identifier;
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        return probe.isConsumed();
    }

    public long getRemainingTokens(String identifier) {
        String key = keyPrefix + identifier;
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(0);
        return probe.getRemainingTokens();
    }

    public long getSecondsToWait(String identifier) {
        String key = keyPrefix + identifier;
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            return 0;
        }
        // Return estimated seconds to wait based on missing tokens
        long tokensNeeded = 1 - probe.getRemainingTokens();
        return Math.max(1, tokensNeeded / requests);
    }

    private Bucket createNewBucket() {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(requests, Refill.intervally(requests, Duration.ofMinutes(duration))))
                .build();
    }
}
