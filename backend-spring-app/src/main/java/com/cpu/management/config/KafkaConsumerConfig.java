package com.cpu.management.config;

import com.cpu.management.dto.event.CpuEventDTO;
import com.cpu.management.dto.event.TechnologyEventDTO;
import com.cpu.management.dto.event.ManufacturerEventDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@ConditionalOnProperty(
    name = "spring.kafka.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.consumer.group-id:default-group}")
    private String groupId;

    // =====================================================
    // CPU Event Consumer
    // =====================================================
    @Bean
    public ConsumerFactory<String, CpuEventDTO> cpuConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, CpuEventDTO.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CpuEventDTO> cpuKafkaListenerContainerFactory(
            ConsumerFactory<String, CpuEventDTO> cpuConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, CpuEventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cpuConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }

    // =====================================================
    // Technology Event Consumer
    // =====================================================
    @Bean
    public ConsumerFactory<String, TechnologyEventDTO> technologyConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TechnologyEventDTO.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TechnologyEventDTO> technologyKafkaListenerContainerFactory(
            ConsumerFactory<String, TechnologyEventDTO> technologyConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TechnologyEventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(technologyConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }

    // =====================================================
    // Manufacturer Event Consumer
    // =====================================================
    @Bean
    public ConsumerFactory<String, ManufacturerEventDTO> manufacturerConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ManufacturerEventDTO.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ManufacturerEventDTO> manufacturerKafkaListenerContainerFactory(
            ConsumerFactory<String, ManufacturerEventDTO> manufacturerConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, ManufacturerEventDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(manufacturerConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }
}
