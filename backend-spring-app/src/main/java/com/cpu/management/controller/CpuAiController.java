package com.cpu.management.controller;

import com.cpu.management.dto.CpuAiAutofillRequestDTO;
import com.cpu.management.dto.CpuAiAutofillResponseDTO;
import com.cpu.management.service.CpuAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cpu-ai")
@RequiredArgsConstructor
@Slf4j
public class CpuAiController {

    private final CpuAiService cpuAiService;

    /**
     * Autofill CPU information using AI
     * POST /api/v1/cpu-ai/autofill
     * 
     * @param request containing the CPU name to autofill
     * @return CPU information populated from AI
     */
    @PostMapping("/autofill")
    public ResponseEntity<CpuAiAutofillResponseDTO> autofillCpuInfo(
            @RequestBody CpuAiAutofillRequestDTO request) {
        
        log.info("Received request to autofill CPU info for: {}", request.getCpuName());
        
        if (request.getCpuName() == null || request.getCpuName().trim().isEmpty()) {
            log.warn("CPU name is empty or null");
            return ResponseEntity.badRequest().build();
        }
        
        try {
            CpuAiAutofillResponseDTO response = cpuAiService.autofillCpuInfo(request.getCpuName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error autofilling CPU info for: {}", request.getCpuName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
