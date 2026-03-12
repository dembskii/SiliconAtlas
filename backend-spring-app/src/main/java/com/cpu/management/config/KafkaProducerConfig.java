package com.cpu.management.config;

import com.cpu.management.dto.event.CpuEventDTO;
import com.cpu.management.dto.event.TechnologyEventDTO;
import com.cpu.management.dto.event.ManufacturerEventDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@ConditionalOnProperty(
    name = "spring.kafka.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapAddress;

    private Map<String, Object> baseProducerProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        return configProps;
    }

    // =====================================================
    // CPU Event Producer
    // =====================================================
    @Bean
    public ProducerFactory<String, CpuEventDTO> cpuProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
            baseProducerProps(),
            new StringSerializer(),
            new JacksonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, CpuEventDTO> cpuKafkaTemplate(
            ProducerFactory<String, CpuEventDTO> cpuProducerFactory) {
        return new KafkaTemplate<>(cpuProducerFactory);
    }

    // =====================================================
    // Technology Event Producer
    // =====================================================
    @Bean
    public ProducerFactory<String, TechnologyEventDTO> technologyProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
            baseProducerProps(),
            new StringSerializer(),
            new JacksonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, TechnologyEventDTO> technologyKafkaTemplate(
            ProducerFactory<String, TechnologyEventDTO> technologyProducerFactory) {
        return new KafkaTemplate<>(technologyProducerFactory);
    }

    // =====================================================
    // Manufacturer Event Producer
    // =====================================================
    @Bean
    public ProducerFactory<String, ManufacturerEventDTO> manufacturerProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
            baseProducerProps(),
            new StringSerializer(),
            new JacksonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, ManufacturerEventDTO> manufacturerKafkaTemplate(
            ProducerFactory<String, ManufacturerEventDTO> manufacturerProducerFactory) {
        return new KafkaTemplate<>(manufacturerProducerFactory);
    }
}
