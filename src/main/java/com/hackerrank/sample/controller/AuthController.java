package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.LoginRequest;
import com.hackerrank.sample.dto.LoginResponse;
import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for security and token generation")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(final JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user and returns a JWT access token.
     * Currently uses hardcoded credentials for demonstration purposes.
     *
     * @param request The login credentials (username and password).
     * @return LoginResponse containing the access token and expiration details.
     * @throws BadResourceRequestException if credentials do not match.
     */
    @Operation(summary = "Login to get access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid final LoginRequest request) {
        if (!"admin".equals(request.username()) || !"admin123".equals(request.password())) {
            throw new BadResourceRequestException("Invalid username or password");
        }

        var token = jwtService.generateToken(request.username());

        return new LoginResponse(token, "Bearer", 3600000L);
    }
}