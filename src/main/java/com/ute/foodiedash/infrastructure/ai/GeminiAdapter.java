package com.ute.foodiedash.infrastructure.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.application.ai.orchestrator.model.AIRequest;
import com.ute.foodiedash.application.ai.orchestrator.model.AIResponse;
import com.ute.foodiedash.application.ai.port.AIChatPort;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiAdapter implements AIChatPort {

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Override
    public AIResponse chat(AIRequest request) {
        String prompt = buildPrompt(request);
        String raw = chatModel.call(prompt);
        return parseAiResponse(raw);
    }

    private String buildPrompt(AIRequest request) {
        return """                
               %s
               
               USER MESSAGE:
               %s
               
               RESPONSE FORMAT:
               IF tool needed:
               {
                    "toolCall": true,
                    "toolName": "...",
                    "toolArguments": {}
               }
               
               OTHERWISE:
               {
                    "toolCall": false,
                    "finalMessage": "..."
               }
               
               """.formatted(request.getSystemPrompt(), request.getUserMessage());
    }

    private AIResponse parseAiResponse(
            String text
    ) {

        try {
            String normalizedText = normalizeJsonText(text);

            JsonNode json =
                    objectMapper.readTree(normalizedText);

            boolean toolCall =
                    json.get("toolCall").asBoolean();

            if (toolCall) {

                return AIResponse.builder()
                        .toolCall(true)
                        .toolName(
                                json.get("toolName").asText()
                        )
                        .toolArguments(
                                json.get("toolArguments")
                                        .toString()
                        )
                        .build();
            }

            return AIResponse.builder()
                    .toolCall(false)
                    .finalMessage(
                            json.get("finalMessage")
                                    .asText()
                    )
                    .build();

        } catch (Exception e) {

            return AIResponse.builder()
                    .toolCall(false)
                    .finalMessage(text)
                    .build();
        }
    }

    private String normalizeJsonText(String text) {
        if (text == null) {
            return "";
        }

        String trimmed = text.trim();

        if (trimmed.startsWith("```")) {
            int firstNewLine = trimmed.indexOf('\n');
            int lastFence = trimmed.lastIndexOf("```");
            if (firstNewLine > -1 && lastFence > firstNewLine) {
                return trimmed.substring(firstNewLine + 1, lastFence).trim();
            }
        }

        return trimmed;
    }
}
