package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Authentication credentials for login")
public record LoginRequest(
        @Schema(description = "User identifier", example = "admin", maxLength = 50)
        @NotBlank(message = "Username is mandatory")
        @Size(max = 50)
        String username,

        @Schema(description = "User password", example = "admin123", maxLength = 100)
        @NotBlank(message = "Password is mandatory")
        @Size(max = 100)
        String password
) {
}