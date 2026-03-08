package com.cpu.management.service.kafka;

import com.cpu.management.dto.event.CpuEventDTO;
import com.cpu.management.dto.event.TechnologyEventDTO;
import com.cpu.management.dto.event.ManufacturerEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    @Autowired(required = false)
    private KafkaTemplate<String, CpuEventDTO> cpuKafkaTemplate;

    @Autowired(required = false)
    private KafkaTemplate<String, TechnologyEventDTO> technologyKafkaTemplate;

    @Autowired(required = false)
    private KafkaTemplate<String, ManufacturerEventDTO> manufacturerKafkaTemplate;

    // Topics
    private static final String TOPIC_CPU_EVENTS = "cpu-events";
    private static final String TOPIC_TECHNOLOGY_EVENTS = "technology-events";
    private static final String TOPIC_MANUFACTURER_EVENTS = "manufacturer-events";

    // CPU Events
    public void publishCpuEvent(CpuEventDTO event) {
        if (cpuKafkaTemplate == null) {
            log.debug("Kafka disabled, skipping CPU event: {}", event);
            return;
        }
        log.info("Publishing CPU event: {}", event);
        cpuKafkaTemplate.send(TOPIC_CPU_EVENTS, event.getCpuId().toString(), event);
    }

    // Technology Events
    public void publishTechnologyEvent(TechnologyEventDTO event) {
        if (technologyKafkaTemplate == null) {
            log.debug("Kafka disabled, skipping Technology event: {}", event);
            return;
        }
        log.info("Publishing Technology event: {}", event);
        technologyKafkaTemplate.send(TOPIC_TECHNOLOGY_EVENTS, event.getTechnologyId().toString(), event);
    }

    // Manufacturer Events
    public void publishManufacturerEvent(ManufacturerEventDTO event) {
        if (manufacturerKafkaTemplate == null) {
            log.debug("Kafka disabled, skipping Manufacturer event: {}", event);
            return;
        }
        log.info("Publishing Manufacturer event: {}", event);
        manufacturerKafkaTemplate.send(TOPIC_MANUFACTURER_EVENTS, event.getManufacturerId().toString(), event);
    }
}
