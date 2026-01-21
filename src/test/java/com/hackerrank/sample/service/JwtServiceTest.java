package com.hackerrank.sample.service;

import com.hackerrank.sample.config.JwtProperties;
import com.hackerrank.sample.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtProperties jwtProperties;

    private JwtService jwtService;

    private final String testSecret = "esta_es_una_clave_de_prueba_muy_larga_y_segura_12345";
    private final long testExpiration = 3600; // 1 hora

    @BeforeEach
    void setUp() {
        when(jwtProperties.secret()).thenReturn(testSecret);
        when(jwtProperties.expiration()).thenReturn(testExpiration);

        jwtService = new JwtService(jwtProperties);
    }

    @Test
    @DisplayName("Should generate a non-null token string")
    void generateToken_Success() {
        var username = "admin";
        var token = jwtService.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should extract the correct username from a valid token")
    void extractUsername_Success() {
        var expectedUsername = "testUser";
        var token = jwtService.generateToken(expectedUsername);

        var actualUsername = jwtService.extractUsername(token);

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    @DisplayName("Should throw InvalidTokenException when token is malformed")
    void extractUsername_MalformedToken() {
        var malformedToken = "not.a.real.jwt.token";

        assertThrows(InvalidTokenException.class, () ->
                jwtService.extractUsername(malformedToken)
        );
    }

    @Test
    @DisplayName("Should throw InvalidTokenException when token is signed with different key")
    void extractUsername_InvalidSignature() {
        var otherProperties = new JwtProperties("otra_clave_totalmente_diferente_abcde_12345", 3600);
        var otherService = new JwtService(otherProperties);
        var tokenFromOtherService = otherService.generateToken("admin");

        assertThrows(InvalidTokenException.class, () ->
                jwtService.extractUsername(tokenFromOtherService)
        );
    }
}