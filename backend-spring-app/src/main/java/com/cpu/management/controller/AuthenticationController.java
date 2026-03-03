package com.cpu.management.controller;

import com.cpu.management.dto.*;
import com.cpu.management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Register a new user
     * POST /api/v1/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        AuthenticationResponseDTO response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login user
     * POST /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        AuthenticationResponseDTO response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh access token
     * POST /api/v1/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshRequest) {
        AuthenticationResponseDTO response = authenticationService.refreshToken(refreshRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout user (client-side token deletion)
     * POST /api/v1/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Token invalidation is handled client-side by deleting the token
        // In a production system, you might want to blacklist tokens or use a token revocation list
        return ResponseEntity.ok().build();
    }
}
