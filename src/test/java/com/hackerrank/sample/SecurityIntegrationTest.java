package com.hackerrank.sample;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "rate.limit.capacity=10",    // Capacidad exacta para el bucle
        "rate.limit.tokens=10"
})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("CP-09: Validate Rate Limiting (10 requests allowed, 11th rejected)")
    void shouldEnforceRateLimiting() throws Exception {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/products"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.message").value("Rate limit exceeded. Try again in a minute."))
                .andExpect(jsonPath("$.status").value(429));
    }
}