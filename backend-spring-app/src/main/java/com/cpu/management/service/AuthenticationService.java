package com.cpu.management.service;

import com.cpu.management.domain.Role;
import com.cpu.management.domain.User;
import com.cpu.management.dto.*;
import com.cpu.management.exception.AuthenticationException;
import com.cpu.management.exception.UserAlreadyExistsException;
import com.cpu.management.repository.UserRepository;
import com.cpu.management.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    // Register new user
    public AuthenticationResponseDTO register(RegisterRequestDTO registerRequest) {
        log.info("Attempting to register user: {}", registerRequest.getUsername());

        // Validate passwords match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new AuthenticationException("Passwords do not match");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + registerRequest.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + registerRequest.getEmail());
        }

        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        // Generate tokens
        String accessToken = jwtProvider.generateToken(savedUser);
        String refreshToken = jwtProvider.generateRefreshToken(savedUser);

        return AuthenticationResponseDTO.of(
                accessToken,
                refreshToken,
                jwtExpirationMs,
                mapToUserResponse(savedUser)
        );
    }

    // Login user
    public AuthenticationResponseDTO login(LoginRequestDTO loginRequest) {
        log.info("Attempting to login user: {}", loginRequest.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getUsername());
            throw new AuthenticationException("Invalid username or password");
        }

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        // Generate tokens
        String accessToken = jwtProvider.generateToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);

        log.info("User logged in successfully: {}", loginRequest.getUsername());

        return AuthenticationResponseDTO.of(
                accessToken,
                refreshToken,
                jwtExpirationMs,
                mapToUserResponse(user)
        );
    }

    // Refresh access token using refresh token
    public AuthenticationResponseDTO refreshToken(RefreshTokenRequestDTO refreshRequest) {
        log.info("Attempting to refresh token");

        String refreshToken = refreshRequest.getRefreshToken();

        if (!jwtProvider.isRefreshTokenValid(refreshToken)) {
            throw new AuthenticationException("Invalid or expired refresh token");
        }

        String username = jwtProvider.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        String newAccessToken = jwtProvider.generateToken(userDetails);

        log.info("Token refreshed successfully for user: {}", username);

        return AuthenticationResponseDTO.of(
                newAccessToken,
                refreshToken,
                jwtExpirationMs,
                mapToUserResponse(user)
        );
    }

    // Maps User entity to UserResponseDTO
    private UserResponseDTO mapToUserResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
