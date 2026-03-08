package com.cpu.management.service.kafka;

import com.cpu.management.dto.event.CpuEventDTO;
import com.cpu.management.dto.event.TechnologyEventDTO;
import com.cpu.management.service.WebSocketService;
import com.cpu.management.dto.event.ManufacturerEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
    name = "spring.kafka.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class KafkaConsumerService {

    private final WebSocketService webSocketService;

    // =====================================================
    // CPU Events Consumer
    // =====================================================
    @KafkaListener(topics = "cpu-events", groupId = "cpu-management-group", containerFactory = "cpuKafkaListenerContainerFactory")
    public void consumeCpuEvent(CpuEventDTO event) {
        log.info("Kafka received CPU event: {} - Type: {}", event.getCpuModel(), event.getEventType());

        try {
            switch(event.getEventType()) {
                case "CREATE":
                    handleCpuCreated(event);
                    break;
                case "UPDATE":
                    handleCpuUpdated(event);
                    break;
                case "DELETE":
                    handleCpuDeleted(event);
                    break;
                default:
                    log.warn("Unknown CPU event type: {}", event.getEventType());
            }
            // Broadcast do WebSocket
            webSocketService.broadcastCpuEvent(event);
        } catch (Exception e) {
            log.error("Error processing CPU event: {}", event, e);
        }
    }

    // =====================================================
    // Technology Events Consumer
    // =====================================================
    @KafkaListener(topics = "technology-events", groupId = "cpu-management-group", containerFactory = "technologyKafkaListenerContainerFactory")
    public void consumeTechnologyEvent(TechnologyEventDTO event) {
        log.info("Kafka received Technology event: {} - Type: {}", event.getTechnologyName(), event.getEventType());

        try {
            switch(event.getEventType()) {
                case "CREATE":
                    handleTechnologyCreated(event);
                    break;
                case "UPDATE":
                    handleTechnologyUpdated(event);
                    break;
                case "DELETE":
                    handleTechnologyDeleted(event);
                    break;
                default:
                    log.warn("Unknown Technology event type: {}", event.getEventType());
            }
            // Broadcast do WebSocket
            webSocketService.broadcastTechnologyEvent(event);
        } catch (Exception e) {
            log.error("Error processing Technology event: {}", event, e);
        }
    }

    // =====================================================
    // Manufacturer Events Consumer
    // =====================================================
    @KafkaListener(topics = "manufacturer-events", groupId = "cpu-management-group", containerFactory = "manufacturerKafkaListenerContainerFactory")
    public void consumeManufacturerEvent(ManufacturerEventDTO event) {
        log.info("Kafka received Manufacturer event: {} - Type: {}", event.getManufacturerName(), event.getEventType());

        try {
            switch(event.getEventType()) {
                case "CREATE":
                    handleManufacturerCreated(event);
                    break;
                case "UPDATE":
                    handleManufacturerUpdated(event);
                    break;
                case "DELETE":
                    handleManufacturerDeleted(event);
                    break;
                default:
                    log.warn("Unknown Manufacturer event type: {}", event.getEventType());
            }
            // Broadcast do WebSocket
            webSocketService.broadcastManufacturerEvent(event);
        } catch (Exception e) {
            log.error("Error processing Manufacturer event: {}", event, e);
        }
    }

    // =====================================================
    // Private handlers for CPU Events
    // =====================================================
    private void handleCpuCreated(CpuEventDTO event) {
        log.info("CPU created: {} by user: {}", event.getCpuModel(), event.getUserId());
        // Możesz dodać dodatkową logikę - logging, metryki, itp.
    }

    private void handleCpuUpdated(CpuEventDTO event) {
        log.info("CPU updated: {} by user: {}", event.getCpuModel(), event.getUserId());
    }

    private void handleCpuDeleted(CpuEventDTO event) {
        log.info("CPU deleted: {} by user: {}", event.getCpuModel(), event.getUserId());
    }

    // =====================================================
    // Private handlers for Technology Events
    // =====================================================
    private void handleTechnologyCreated(TechnologyEventDTO event) {
        log.info("Technology created: {} by user: {}", event.getTechnologyName(), event.getUserId());
    }

    private void handleTechnologyUpdated(TechnologyEventDTO event) {
        log.info("Technology updated: {} by user: {}", event.getTechnologyName(), event.getUserId());
    }

    private void handleTechnologyDeleted(TechnologyEventDTO event) {
        log.info("Technology deleted: {} by user: {}", event.getTechnologyName(), event.getUserId());
    }

    // =====================================================
    // Private handlers for Manufacturer Events
    // =====================================================
    private void handleManufacturerCreated(ManufacturerEventDTO event) {
        log.info("Manufacturer created: {} by user: {}", event.getManufacturerName(), event.getUserId());
    }

    private void handleManufacturerUpdated(ManufacturerEventDTO event) {
        log.info("Manufacturer updated: {} by user: {}", event.getManufacturerName(), event.getUserId());
    }

    private void handleManufacturerDeleted(ManufacturerEventDTO event) {
        log.info("Manufacturer deleted: {} by user: {}", event.getManufacturerName(), event.getUserId());
    }
}
