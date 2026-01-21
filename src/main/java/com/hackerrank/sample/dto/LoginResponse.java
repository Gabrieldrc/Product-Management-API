package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Security token details after successful authentication")
public record LoginResponse(
        @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,

        @Schema(description = "Type of token provided", example = "Bearer")
        String tokenType,

        @Schema(description = "Expiration time in milliseconds", example = "3600000")
        long expiresIn
) {
}