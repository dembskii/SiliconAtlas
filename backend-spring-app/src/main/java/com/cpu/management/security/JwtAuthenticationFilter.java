package com.cpu.management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Public paths that don't require authentication
     */
    private static final String[] PUBLIC_PATHS = {
            "/v3/api-docs",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/swagger-initializer.js",
            "/api/v1/auth/",
            "/login",
            "/register",
            "/static/",
            "/js/",
            "/css/",
            "/images/",
            "/ws/",
            "/health",
            "/actuator/"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestPath = request.getRequestURI();
        for (String publicPath : PUBLIC_PATHS) {
            if (requestPath.startsWith(publicPath)) {
                log.debug("Skipping JWT filter for public path: {}", requestPath);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);
            log.debug("JWT extracted from request. Token present: {}", jwt != null);
            
            if (jwt != null) {
                try {
                    String username = jwtProvider.extractUsername(jwt);
                    log.debug("JWT username extracted: {}", username);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        log.debug("UserDetails loaded for: {}", username);
                        
                        if (jwtProvider.isTokenValid(jwt, userDetails)) {
                            SecurityContext context = SecurityContextHolder.createEmptyContext();
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities());
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            context.setAuthentication(authToken);
                            SecurityContextHolder.setContext(context);
                            log.info("JWT Token validated and authenticated for user: {}", username);
                        } else {
                            log.warn("JWT Token validation FAILED for user: {}", username);
                        }
                    } else {
                        log.debug("Username is null or authentication already exists");
                    }
                } catch (Exception e) {
                    log.error("JWT Token validation exception for token: {}", jwt.substring(0, 30), e);
                }
            }
        } catch (Exception e) {
            log.error("JWT Token extraction/validation failed: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from Authorization header or cookies
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        // Try Authorization header first
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        // Fallback to cookies
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null && !token.isEmpty()) {
                        log.debug("JWT Token extracted from cookie");
                        return token;
                    }
                }
            }
        }
        
        return null;
    }
}
