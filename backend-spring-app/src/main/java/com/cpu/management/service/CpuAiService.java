package com.cpu.management.service;

import com.cpu.management.domain.Manufacturer;
import com.cpu.management.domain.Technology;
import com.cpu.management.dto.CpuAiAutofillResponseDTO;
import com.cpu.management.repository.ManufacturerRepository;
import com.cpu.management.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpuAiService {

    private final ChatModel chatModel;
    private final ManufacturerRepository manufacturerRepository;
    private final TechnologyRepository technologyRepository;

    /**
     * Autofill CPU information using Google Gemini AI
     * @param cpuName the name of the CPU to search for
     * @return CpuAiAutofillResponseDTO with CPU details
     */
    public CpuAiAutofillResponseDTO autofillCpuInfo(String cpuName) {
        try {
            List<String> possibleManufacturers = getPossibleManufacturerNames();
            List<String> possibleTechnologies = getPossibleTechnologyNames();

            // Build the prompt for Gemini
            String promptText = buildPrompt(cpuName, possibleManufacturers, possibleTechnologies);
            
            // Call Gemini API
            Prompt prompt = new Prompt(promptText);
            String response = chatModel.call(prompt).getResult().getOutput().getText();
            
            log.info("AI Response for CPU {}: {}", cpuName, response);
            
            // Parse the response and build the DTO
            return parseAiResponse(cpuName, response, possibleManufacturers, possibleTechnologies);
        } catch (Exception e) {
            log.error("Error fetching CPU info from AI for CPU: {}", cpuName, e);
            // Return a basic response with default values if AI fails
            return createDefaultResponse(cpuName);
        }
    }

    /**
     * Build a structured prompt for Gemini to get CPU information
     */
    private String buildPrompt(String cpuName, List<String> possibleManufacturers, List<String> possibleTechnologies) {
        String manufacturerList = possibleManufacturers.isEmpty()
                ? "[]"
                : possibleManufacturers.stream().map(name -> "\"" + name + "\"").collect(Collectors.joining(", ", "[", "]"));

        String technologyList = possibleTechnologies.isEmpty()
                ? "[]"
                : possibleTechnologies.stream().map(name -> "\"" + name + "\"").collect(Collectors.joining(", ", "[", "]"));

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
            "  \"manufacturerName\": \"one of the provided manufacturers\",\n" +
            "  \"technologies\": [\"technology1\", \"technology2\"]\n" +
            "}\n\n" +
            "Allowed manufacturers (must use exactly one from this list): %s\n" +
            "Allowed technologies (use only values from this list in the technologies array): %s\n\n" +
            "Important rules:\n" +
            "1. Return technologies as a JSON array of strings.\n" +
            "2. Do not invent manufacturer or technology names outside the provided lists.\n" +
            "3. If uncertain, use an empty technologies array [] and choose the closest allowed manufacturer.\n" +
            "4. Return only raw JSON, without markdown code fences.\n",
            cpuName,
            manufacturerList,
            technologyList
        );
    }

    /**
     * Parse the AI response and extract CPU information
     */
    private CpuAiAutofillResponseDTO parseAiResponse(
            String cpuName,
            String response,
            List<String> possibleManufacturers,
            List<String> possibleTechnologies
    ) {
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
            dto.setManufacturerName(normalizeToAllowedValue(manufacturer, possibleManufacturers));
            
            // Extract technologies as list
            java.util.List<String> technologies = extractJsonArray(jsonContent, "technologies");
            dto.setTechnologies(normalizeTechnologies(technologies, possibleTechnologies));
            
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
        String pattern = "\"" + fieldName + "\"\\s*:\\s*\\[([^\\]]*)\\]";
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

    private List<String> getPossibleManufacturerNames() {
        return manufacturerRepository.findAll().stream()
                .map(Manufacturer::getName)
                .filter(name -> name != null && !name.isBlank())
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private List<String> getPossibleTechnologyNames() {
        return technologyRepository.findAll().stream()
                .map(Technology::getName)
                .filter(name -> name != null && !name.isBlank())
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private String normalizeToAllowedValue(String candidate, List<String> allowedValues) {
        if (candidate == null || candidate.isBlank() || allowedValues == null || allowedValues.isEmpty()) {
            return "";
        }

        String normalizedCandidate = candidate.trim().toLowerCase(Locale.ROOT);
        for (String allowed : allowedValues) {
            if (allowed.toLowerCase(Locale.ROOT).equals(normalizedCandidate)) {
                return allowed;
            }
        }

        return "";
    }

    private List<String> normalizeTechnologies(List<String> candidates, List<String> allowedTechnologies) {
        if (candidates == null || candidates.isEmpty()) {
            return Collections.emptyList();
        }

        if (allowedTechnologies == null || allowedTechnologies.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, String> allowedByLower = allowedTechnologies.stream()
                .collect(Collectors.toMap(
                        value -> value.toLowerCase(Locale.ROOT),
                        Function.identity(),
                        (left, right) -> left
                ));

        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String candidate : candidates) {
            if (candidate == null || candidate.isBlank()) {
                continue;
            }

            String match = allowedByLower.get(candidate.trim().toLowerCase(Locale.ROOT));
            if (match != null) {
                normalized.add(match);
            }
        }

        return new ArrayList<>(normalized);
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
                .technologies(Collections.emptyList())
                .build();
    }
}
