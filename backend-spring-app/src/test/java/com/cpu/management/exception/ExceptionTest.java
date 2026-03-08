package com.cpu.management.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void shouldCreateCpuNotFoundExceptionWithUUID() {
        UUID id = UUID.randomUUID();
        CpuNotFoundException exception = new CpuNotFoundException(id);

        assertTrue(exception.getMessage().contains(id.toString()));
        assertEquals(id, exception.getCpuId());
    }

    @Test
    void shouldCreateCpuNotFoundExceptionWithModel() {
        CpuNotFoundException exception = new CpuNotFoundException("Ryzen 9 7950X");

        assertTrue(exception.getMessage().contains("Ryzen 9 7950X"));
        assertNull(exception.getCpuId());
    }

    @Test
    void shouldCreateDuplicateCpuModelException() {
        DuplicateCpuModelException exception = new DuplicateCpuModelException("Core i9-13900K");

        assertTrue(exception.getMessage().contains("Core i9-13900K"));
        assertEquals("Core i9-13900K", exception.getModel());
    }

    @Test
    void shouldCreateAuthenticationException() {
        AuthenticationException exception = new AuthenticationException("Invalid credentials");

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void shouldCreateAuthenticationExceptionWithCause() {
        Throwable cause = new RuntimeException("Original error");
        AuthenticationException exception = new AuthenticationException("Auth failed", cause);

        assertEquals("Auth failed", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateUserAlreadyExistsException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User exists");

        assertEquals("User exists", exception.getMessage());
    }

    @Test
    void shouldCreateUserAlreadyExistsExceptionWithCause() {
        Throwable cause = new RuntimeException("DB constraint");
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User exists", cause);

        assertEquals("User exists", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
