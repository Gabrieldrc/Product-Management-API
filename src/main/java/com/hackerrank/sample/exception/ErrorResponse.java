package com.hackerrank.sample.exception;

public record ErrorResponse(
        int status,
        String message,
        long timestamp
) {
}
