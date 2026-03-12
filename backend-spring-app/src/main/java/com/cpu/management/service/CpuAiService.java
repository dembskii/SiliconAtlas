package com.cpu.management.service;

import com.cpu.management.dto.AiPromptRequestDTO;
import com.cpu.management.dto.AiResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpuAiService {
    
    private final ChatModel chatModel;
    
    @Value("${spring.ai.google.genai.chat.options.model:gemini-2.0-flash}")
    private String modelName;

    /**
     * Send a prompt to Google Gemini AI and get a response
     */
    public AiResponseDTO askAi(AiPromptRequestDTO request) {
        log.debug("Sending AI request: {}", request.getMessage());

        // Set default values if not provided
        if (request.getTemperature() == null) {
            request.setTemperature(0.7);
        }
        if (request.getMaxTokens() == null) {
            request.setMaxTokens(1000);
        }

        try {
            // Configure chat options with user parameters
            GoogleGenAiChatOptions options = GoogleGenAiChatOptions.builder()
                    .withModel(modelName)
                    .withTemperature(request.getTemperature().floatValue())
                    .withMaxRetries(3)
                    .build();

            Prompt prompt = new Prompt(request.getMessage(), options);
            ChatResponse response = chatModel.call(prompt);

            AiResponseDTO aiResponse = new AiResponseDTO();
            aiResponse.setResponse(response.getResult().getOutput().getContent());
            aiResponse.setPrompt(request.getMessage());
            aiResponse.setTimestamp(LocalDateTime.now());
            aiResponse.setModel(modelName);
            aiResponse.setTemperature(request.getTemperature());

            log.debug("AI response generated successfully");
            return aiResponse;

        } catch (Exception e) {
            log.error("Error calling AI service", e);
            throw new RuntimeException("Failed to get AI response: " + e.getMessage(), e);
        }
    }
}
