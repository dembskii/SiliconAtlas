package com.cpu.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable in memory message broker with destination prefix "/topic"
        config.enableSimpleBroker("/topic");
        
        // Destination prefix for messages sent from clients to server
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register enpoint for websocket connections
        registry.addEndpoint("/ws/events")
                .setAllowedOriginPatterns("*")  // Allow cors for all origins
                .withSockJS();           // Fallback to SockJS if WebSocket is not available
    }
}
