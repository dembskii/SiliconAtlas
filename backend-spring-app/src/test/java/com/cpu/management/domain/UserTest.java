package com.cpu.management.domain;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithBuilder() {
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("encoded_password")
                .role(Role.USER)
                .build();

        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("encoded_password", user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void shouldReturnCorrectAuthoritiesForUser() {
        User user = User.builder()
                .username("testuser")
                .role(Role.USER)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    void shouldReturnCorrectAuthoritiesForAdmin() {
        User user = User.builder()
                .username("admin")
                .role(Role.ADMIN)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void shouldReturnTrueForAccountStatus() {
        User user = User.builder()
                .username("testuser")
                .role(Role.USER)
                .build();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}
