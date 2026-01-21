package com.hackerrank.sample.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final int capacity;
    private final int tokensPerMinute;

    public RateLimitingFilter(
            @Value("${rate.limit.capacity:100}") final int capacity,
            @Value("${rate.limit.tokens:100}") final int tokensPerMinute) {
        this.capacity = capacity;
        this.tokensPerMinute = tokensPerMinute;
    }

    /**
     * Applies rate limiting logic specifically to product endpoints
     * using the Token Bucket algorithm.
     */
    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {

        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        var requestUri = httpRequest.getRequestURI();

        if (!requestUri.startsWith("/products")) {
            chain.doFilter(request, response);
            return;
        }

        var clientIp = httpRequest.getRemoteAddr();
        var bucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
            return;
        }

        sendRateLimitError(httpResponse);
    }

    private Bucket createNewBucket() {
        var refill = Refill.greedy(tokensPerMinute, Duration.ofMinutes(1));
        var limit = Bandwidth.classic(capacity, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    private void sendRateLimitError(final HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

        var jsonResponse = """
                {
                    "type": "about:blank",
                    "title": "Too Many Requests",
                    "status": 429,
                    "detail": "Rate limit exceeded. Try again in a minute.",
                    "instance": "/products",
                    "timestamp": "%s"
                }
                """.formatted(Instant.now());

        response.getWriter().write(jsonResponse);
    }
}