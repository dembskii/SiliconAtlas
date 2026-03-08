package com.cpu.management.config;

import com.cpu.management.dto.event.CpuEventDTO;
import com.cpu.management.dto.event.TechnologyEventDTO;
import com.cpu.management.dto.event.ManufacturerEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.kafka.support.serializer.JsonSerializer;

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

    private ObjectMapper kafkaObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    // =====================================================
    // CPU Event Producer
    // =====================================================
    @Bean
    public ProducerFactory<String, CpuEventDTO> cpuProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        
        JsonSerializer<CpuEventDTO> serializer = new JsonSerializer<>(kafkaObjectMapper());
        serializer.setAddTypeInfo(true);
        
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, CpuEventDTO> cpuKafkaTemplate() {
        return new KafkaTemplate<>(cpuProducerFactory());
    }

    // =====================================================
    // Technology Event Producer
    // =====================================================
    @Bean
    public ProducerFactory<String, TechnologyEventDTO> technologyProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        
        JsonSerializer<TechnologyEventDTO> serializer = new JsonSerializer<>(kafkaObjectMapper());
        serializer.setAddTypeInfo(true);
        
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, TechnologyEventDTO> technologyKafkaTemplate() {
        return new KafkaTemplate<>(technologyProducerFactory());
    }

    // =====================================================
    // Manufacturer Event Producer
    // =====================================================
    @Bean
    public ProducerFactory<String, ManufacturerEventDTO> manufacturerProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        
        JsonSerializer<ManufacturerEventDTO> serializer = new JsonSerializer<>(kafkaObjectMapper());
        serializer.setAddTypeInfo(true);
        
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), serializer);
    }

    @Bean
    public KafkaTemplate<String, ManufacturerEventDTO> manufacturerKafkaTemplate() {
        return new KafkaTemplate<>(manufacturerProducerFactory());
    }
}
