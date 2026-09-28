package com.workouttracker.service;

import com.workouttracker.domain.User;
import com.workouttracker.dto.Dtos;
import com.workouttracker.repository.UserRepository;
import com.workouttracker.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks private AuthService authService;

    @Test
    void shouldRegisterNewUser() {
        UUID userId = UUID.randomUUID();
        Dtos.RegisterRequest request =
                new Dtos.RegisterRequest("tilek@example.com", "tilek", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("hashed-password");

        User savedUser = User.builder()
                .id(userId)
                .email(request.email())
                .username(request.username())
                .passwordHash("hashed-password")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generate(userId)).thenReturn("jwt-token");

        Dtos.AuthResponse response = authService.register(request);

        assertThat(response.id()).isEqualTo(userId);
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.username()).isEqualTo(request.username());
        assertThat(response.token()).isEqualTo("jwt-token");

        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));
    }
}
