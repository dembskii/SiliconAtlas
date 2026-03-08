package com.cpu.management;

import com.cpu.management.service.kafka.KafkaProducerService;
import com.cpu.management.service.WebSocketService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

/**
 * Test configuration dla Kafka i WebSocket
 * Mocka serwisy dla environment testowego
 */
@TestConfiguration
public class KafkaTestConfig {

    @Bean
    @Primary
    public KafkaProducerService kafkaProducerService() {
        return mock(KafkaProducerService.class);
    }

    @Bean
    @Primary
    public WebSocketService webSocketService() {
        return mock(WebSocketService.class);
    }
}
