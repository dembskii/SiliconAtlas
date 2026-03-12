package com.cpu.management.controller;

import com.cpu.management.dto.AiPromptRequestDTO;
import com.cpu.management.dto.AiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class CpuAiController {
    
    private final ChatModel chatModel;
    
    @Value("${spring.ai.google.genai.chat.options.model:gemini-2.0-flash}")
    private String modelName;

    /**
     * POST endpoint for AI chat - accepts JSON body with prompt details
     */
    @PostMapping("/chat")
    public ResponseEntity<AiResponseDTO> chatWithAi(@RequestBody AiPromptRequestDTO request) {
        if (request.getTemperature() == null) {
            request.setTemperature(0.7);
        }
        if (request.getMaxTokens() == null) {
            request.setMaxTokens(1000);
        }

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

        return ResponseEntity.ok(aiResponse);
    }

    /**
     * GET endpoint for simple text queries
     */
    @GetMapping("/chat")
    public ResponseEntity<AiResponseDTO> chatWithAi(@RequestParam String message) {
        AiPromptRequestDTO request = new AiPromptRequestDTO();
        request.setMessage(message);
        request.setTemperature(0.7);
        request.setMaxTokens(1000);

        return chatWithAi(request);
    }
}
