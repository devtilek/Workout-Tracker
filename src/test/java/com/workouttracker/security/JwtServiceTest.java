package com.workouttracker.security;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService =
            new JwtService("test-secret-key-that-is-at-least-32-characters-long", 60_000);

    @Test
    void shouldGenerateAndParseUserId() {
        UUID userId = UUID.randomUUID();

        String token = jwtService.generate(userId);

        assertThat(token).isNotBlank();
        assertThat(jwtService.parseUserId(token)).isEqualTo(userId);
    }
}
