package com.hackerrank.sample;

import com.hackerrank.sample.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "rate.limit.capacity=10",
        "rate.limit.tokens=10"
})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("SEC-01: Public endpoints should be accessible without token")
    void shouldAllowPublicAccess() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("SEC-02: Protected endpoints should return 401 when no token is provided")
    void shouldRejectUnauthenticatedPost() throws Exception {
        final var productJson = """
                {
                    "title": "Unauthorized Product",
                    "price": 100.0,
                    "stock": 10,
                    "condition": "NEW",
                    "imageUrls": []
                }
                """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("SEC-03: Protected endpoints should return 401 when token is invalid")
    void shouldRejectInvalidToken() throws Exception {
        mockMvc.perform(delete("/products/1")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("SEC-04: Protected endpoints should allow access with valid JWT")
    void shouldAllowAccessWithValidToken() throws Exception {
        final var token = jwtService.generateToken("admin");
        final var productJson = """
                {
                    "title": "Authorized Product",
                    "price": 500.0,
                    "stock": 5,
                    "condition": "NEW",
                    "imageUrls": ["https://example.com/image.png"]
                }
                """;

        mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("CP-09: Validate Rate Limiting (10 requests allowed, 11th rejected)")
    void shouldEnforceRateLimiting() throws Exception {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/products"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.detail").value("Rate limit exceeded. Try again in a minute."))
                .andExpect(jsonPath("$.status").value(429))
                .andExpect(jsonPath("$.title").value("Too Many Requests"));
    }
}