package com.cpu.management.config;

import com.cpu.management.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String authorizationHeader = resolveAuthorizationHeader(accessor);
        if (authorizationHeader == null || !authorizationHeader.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            log.warn("Rejecting STOMP CONNECT: missing or invalid Authorization header");
            throw new MessageDeliveryException(message, "Missing or invalid Authorization header in STOMP CONNECT");
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        try {
            String username = jwtProvider.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!jwtProvider.isTokenValid(token, userDetails)) {
                throw new MessageDeliveryException(message, "Invalid JWT token in STOMP CONNECT");
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            accessor.setUser(authentication);
            log.debug("STOMP CONNECT authenticated for user: {}", username);
            return message;
        } catch (Exception ex) {
            log.warn("Rejecting STOMP CONNECT due to invalid JWT", ex);
            throw new MessageDeliveryException(message, "Invalid JWT token in STOMP CONNECT", ex);
        }
    }

    private String resolveAuthorizationHeader(StompHeaderAccessor accessor) {
        String header = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && !header.isBlank()) {
            return header;
        }

        header = accessor.getFirstNativeHeader("authorization");
        if (header != null && !header.isBlank()) {
            return header;
        }

        return null;
    }
}
