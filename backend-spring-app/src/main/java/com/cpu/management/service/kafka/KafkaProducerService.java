package com.cpu.management.service.kafka;

import com.cpu.management.dto.event.CpuEventDTO;
import com.cpu.management.dto.event.TechnologyEventDTO;
import com.cpu.management.dto.event.ManufacturerEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, CpuEventDTO> cpuKafkaTemplate;
    private final KafkaTemplate<String, TechnologyEventDTO> technologyKafkaTemplate;
    private final KafkaTemplate<String, ManufacturerEventDTO> manufacturerKafkaTemplate;

    // Topics
    private static final String TOPIC_CPU_EVENTS = "cpu-events";
    private static final String TOPIC_TECHNOLOGY_EVENTS = "technology-events";
    private static final String TOPIC_MANUFACTURER_EVENTS = "manufacturer-events";

    // CPU Events
    public void publishCpuEvent(CpuEventDTO event) {
        log.info("Publishing CPU event: {}", event);
        cpuKafkaTemplate.send(TOPIC_CPU_EVENTS, event.getCpuId().toString(), event);
    }

    // Technology Events
    public void publishTechnologyEvent(TechnologyEventDTO event) {
        log.info("Publishing Technology event: {}", event);
        technologyKafkaTemplate.send(TOPIC_TECHNOLOGY_EVENTS, event.getTechnologyId().toString(), event);
    }

    // Manufacturer Events
    public void publishManufacturerEvent(ManufacturerEventDTO event) {
        log.info("Publishing Manufacturer event: {}", event);
        manufacturerKafkaTemplate.send(TOPIC_MANUFACTURER_EVENTS, event.getManufacturerId().toString(), event);
    }
}
