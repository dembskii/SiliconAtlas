package com.cpu.management.service;

import com.cpu.management.dto.CpuAiAutofillResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpuAiService {

    private final ChatModel chatModel;

    /**
     * Autofill CPU information using Google Gemini AI
     * @param cpuName the name of the CPU to search for
     * @return CpuAiAutofillResponseDTO with CPU details
     */
    public CpuAiAutofillResponseDTO autofillCpuInfo(String cpuName) {
        try {
            // Build the prompt for Gemini
            String promptText = buildPrompt(cpuName);
            
            // Call Gemini API
            Prompt prompt = new Prompt(promptText);
            String response = chatModel.call(prompt).getResult().getOutput().getText();
            
            log.info("AI Response for CPU {}: {}", cpuName, response);
            
            // Parse the response and build the DTO
            return parseAiResponse(cpuName, response);
        } catch (Exception e) {
            log.error("Error fetching CPU info from AI for CPU: {}", cpuName, e);
            // Return a basic response with default values if AI fails
            return createDefaultResponse(cpuName);
        }
    }

    /**
     * Build a structured prompt for Gemini to get CPU information
     */
    private String buildPrompt(String cpuName) {
        return String.format(
            "Provide detailed technical specifications for the CPU: %s\n\n" +
            "Please respond in JSON format with the following fields (use 0 for unknown values):\n" +
            "{\n" +
            "  \"model\": \"CPU model name\",\n" +
            "  \"cores\": number,\n" +
            "  \"threads\": number,\n" +
            "  \"frequencyGhz\": decimal number,\n" +
            "  \"cacheL1KB\": number,\n" +
            "  \"cacheL2KB\": number,\n" +
            "  \"cacheL3MB\": number,\n" +
            "  \"tdpWatts\": number,\n" +
            "  \"socketType\": \"socket name\",\n" +
            "  \"manufacturerName\": \"Intel or AMD\",\n" +
            "  \"technologies\": [\"technology1\", \"technology2\"]\n" +
            "}\n\n" +
            "Be accurate and provide only valid technical specifications.",
            cpuName
        );
    }

    /**
     * Parse the AI response and extract CPU information
     */
    private CpuAiAutofillResponseDTO parseAiResponse(String cpuName, String response) {
        try {
            // Extract JSON from response (remove markdown code blocks if present)
            String jsonContent = response.replace("```json", "").replace("```", "").trim();
            
            // Parse JSON using a simple approach (in production, use Jackson or similar)
            CpuAiAutofillResponseDTO dto = new CpuAiAutofillResponseDTO();
            
            // Extract model
            String model = extractJsonField(jsonContent, "model");
            dto.setModel(model != null ? model : cpuName);
            
            // Extract numeric fields
            dto.setCores(extractJsonInt(jsonContent, "cores"));
            dto.setThreads(extractJsonInt(jsonContent, "threads"));
            dto.setFrequencyGhz(extractJsonDouble(jsonContent, "frequencyGhz"));
            dto.setCacheL1KB(extractJsonInt(jsonContent, "cacheL1KB"));
            dto.setCacheL2KB(extractJsonInt(jsonContent, "cacheL2KB"));
            dto.setCacheL3MB(extractJsonInt(jsonContent, "cacheL3MB"));
            dto.setTdpWatts(extractJsonInt(jsonContent, "tdpWatts"));
            
            // Extract socket type
            String socketType = extractJsonField(jsonContent, "socketType");
            dto.setSocketType(socketType);
            
            // Extract manufacturer
            String manufacturer = extractJsonField(jsonContent, "manufacturerName");
            dto.setManufacturerName(manufacturer);
            
            // Extract technologies as list
            java.util.List<String> technologies = extractJsonArray(jsonContent, "technologies");
            dto.setTechnologies(technologies);
            
            return dto;
        } catch (Exception e) {
            log.error("Error parsing AI response: {}", response, e);
            return createDefaultResponse(cpuName);
        }
    }

    /**
     * Extract a string field from JSON response
     */
    private String extractJsonField(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Extract an integer field from JSON response
     */
    private int extractJsonInt(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\"\\s*:\\s*(\\d+)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    /**
     * Extract a double field from JSON response
     */
    private double extractJsonDouble(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\"\\s*:\\s*([\\d.]+)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(json);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return 0.0;
    }

    /**
     * Extract a JSON array field from response
     */
    private java.util.List<String> extractJsonArray(String json, String fieldName) {
        java.util.List<String> result = new java.util.ArrayList<>();
        String pattern = "\"" + fieldName + "\"\\s*:\\s*\\[([^\\]]+)\\]";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(json);
        
        if (matcher.find()) {
            String arrayContent = matcher.group(1);
            String[] items = arrayContent.split(",");
            for (String item : items) {
                String cleaned = item.trim().replaceAll("\"", "");
                if (!cleaned.isEmpty()) {
                    result.add(cleaned);
                }
            }
        }
        return result;
    }

    /**
     * Create a default response when AI lookup fails
     */
    private CpuAiAutofillResponseDTO createDefaultResponse(String cpuName) {
        return CpuAiAutofillResponseDTO.builder()
                .model(cpuName)
                .cores(0)
                .threads(0)
                .frequencyGhz(0.0)
                .cacheL1KB(0)
                .cacheL2KB(0)
                .cacheL3MB(0)
                .tdpWatts(0)
                .socketType("")
                .manufacturerName("")
                .technologies(java.util.Collections.emptyList())
                .build();
    }
}
