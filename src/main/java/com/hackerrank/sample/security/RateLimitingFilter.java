package com.hackerrank.sample.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final int capacity;
    private final int tokensPerMinute;

    public RateLimitingFilter(
            @Value("${rate.limit.capacity:100}") int capacity,
            @Value("${rate.limit.tokens:100}") int tokensPerMinute) {
        this.capacity = capacity;
        this.tokensPerMinute = tokensPerMinute;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().startsWith("/products")) {
            String clientIp = httpRequest.getRemoteAddr();
            Bucket bucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());

            if (bucket.tryConsume(1)) {
                chain.doFilter(request, response);
            } else {
                sendRateLimitError(httpResponse);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, Refill.greedy(tokensPerMinute, Duration.ofMinutes(1))))
                .build();
    }

    private void sendRateLimitError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");

        long timestamp = java.time.Instant.now().toEpochMilli();
        String json = String.format("""
                {
                    "status": 429,
                    "message": "Rate limit exceeded. Try again in a minute.",
                    "timestamp": %d
                }
                """, timestamp);

        response.getWriter().write(json);
    }
}