package com.cpu.management.service;

import com.cpu.management.dto.event.CpuEventDTO;
import com.cpu.management.dto.event.TechnologyEventDTO;
import com.cpu.management.dto.event.ManufacturerEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // Topics
    private static final String TOPIC_CPU_EVENTS = "/topic/cpu-events";
    private static final String TOPIC_TECHNOLOGY_EVENTS = "/topic/technology-events";
    private static final String TOPIC_MANUFACTURER_EVENTS = "/topic/manufacturer-events";
    private static final String TOPIC_ALL_EVENTS = "/topic/all-events";

    // CPU Events Broadcasting
    public void broadcastCpuEvent(CpuEventDTO event) {
        log.info("Broadcasting CPU event via WebSocket: {}", event.getCpuModel());
        try {
            messagingTemplate.convertAndSend(TOPIC_CPU_EVENTS, event);
            messagingTemplate.convertAndSend(TOPIC_ALL_EVENTS, event);
        } catch (Exception e) {
            log.error("Error broadcasting CPU event: {}", event, e);
        }
    }


    // Technology Events Broadcasting
    public void broadcastTechnologyEvent(TechnologyEventDTO event) {
        log.info("Broadcasting Technology event via WebSocket: {}", event.getTechnologyName());
        try {
            messagingTemplate.convertAndSend(TOPIC_TECHNOLOGY_EVENTS, event);
            messagingTemplate.convertAndSend(TOPIC_ALL_EVENTS, event);
        } catch (Exception e) {
            log.error("Error broadcasting Technology event: {}", event, e);
        }
    }


    // Manufacturer Events Broadcasting
    public void broadcastManufacturerEvent(ManufacturerEventDTO event) {
        log.info("Broadcasting Manufacturer event via WebSocket: {}", event.getManufacturerName());
        try {
            messagingTemplate.convertAndSend(TOPIC_MANUFACTURER_EVENTS, event);
            messagingTemplate.convertAndSend(TOPIC_ALL_EVENTS, event);
        } catch (Exception e) {
            log.error("Error broadcasting Manufacturer event: {}", event, e);
        }
    }
}
