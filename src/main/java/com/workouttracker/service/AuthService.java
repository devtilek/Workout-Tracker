package com.workouttracker.service;

import com.workouttracker.domain.User;
import com.workouttracker.dto.Dtos;
import com.workouttracker.repository.UserRepository;
import com.workouttracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public Dtos.AuthResponse register(Dtos.RegisterRequest request){
        if (userRepository.existsByEmail(request.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already used");
        if (userRepository.existsByUsername(request.username()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already used");

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        user = userRepository.save(user);

        return new Dtos.AuthResponse(
                user.getId(), user.getEmail(), user.getUsername(), jwtService.generate(user.getId())
        );
    }
    @Transactional(readOnly = true)
    public Dtos.AuthResponse login(Dtos.LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        return new Dtos.AuthResponse(
                user.getId(), user.getEmail(), user.getUsername(),
                jwtService.generate(user.getId()));
    }
}
