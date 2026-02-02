package com.ute.foodiedash.application.ai.orchestrator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.application.ai.dto.AIChatResponseDTO;
import com.ute.foodiedash.application.ai.orchestrator.model.AIRequest;
import com.ute.foodiedash.application.ai.orchestrator.model.AIResponse;
import com.ute.foodiedash.application.ai.port.AIChatPort;
import com.ute.foodiedash.application.ai.tool.McpToolDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AIOrchestrator {
    private final AIChatPort aiChatPort;

    private final McpToolDispatcher dispatcher;

    private final ObjectMapper objectMapper;

    public AIChatResponseDTO process(
            String message
    ) {

        String systemPrompt = """
                You are FoodieDash AI Assistant.
                
                Use tools whenever real data is needed.
                
                TOOL RULES:
                - search_restaurant: for restaurants, places, cuisines
                - search_menu_item: for foods, dishes, drinks
                - track_order: for delivery tracking
                - create_order: for placing orders
                
                TOOLS:
                
                1. search_restaurant
                
                Arguments:
                {
                  "keyword": "string|null",
                  "categoryName": "string|null",
                  "lat": "number|null",
                  "lng": "number|null",
                  "radius": "number|null",
                  "minRating": "number|null",
                  "isOpen": "boolean|null",
                  "limit": "number|null",
                  "offset": "number|null"
                }
                
                2. search_menu_item
                
                Arguments:
                {
                  "itemName": "string",
                  "minPrice": "number|null",
                  "maxPrice": "number|null"
                }
                
                3. track_order
                
                Arguments:
                {
                  "orderCode": "string"
                }
                
                4. create_order
                
                Arguments:
                {
                  "restaurantId": "number",
                  "items": [
                    {
                      "menuItemId": "number",
                      "quantity": "number"
                    }
                  ],
                  "paymentMethod": "string",
                  "deliveryAddress": "string"
                }
                
                RESPONSE FORMAT:
                
                If tool is needed:
                {
                  "toolCall": true,
                  "toolName": "...",
                  "toolArguments": {}
                }
                
                If tool is NOT needed:
                {
                  "toolCall": false,
                  "finalMessage": "..."
                }
                
                Return ONLY valid JSON.
                """;

        AIRequest aiRequest = AIRequest
                .builder()
                .systemPrompt(systemPrompt)
                .userMessage(message)
                .build();

        AIResponse aiResponse = aiChatPort.chat(aiRequest);

        if (aiResponse.isToolCall()) {

            try {

                Map<String, Object> args =
                        objectMapper.readValue(
                                aiResponse.getToolArguments(),
                                new TypeReference<>() {}
                        );

                Object result =
                        dispatcher.dispatch(
                                aiResponse.getToolName(),
                                args
                        );

                String finalMessage = generateFinalResponse(
                        message,
                        aiResponse.getToolName(),
                        result
                );

                return AIChatResponseDTO.builder()
                        .message(finalMessage)
                        .type(resolveResponseType(aiResponse.getToolName()))
                        .data(result)
                        .build();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return AIChatResponseDTO.builder()
                .message(aiResponse.getFinalMessage())
                .type("TEXT")
                .data(null)
                .build();
    }

    private String resolveResponseType(String toolName) {
        if (toolName == null || toolName.isBlank()) {
            return "TEXT";
        }
        return switch (toolName) {
            case "search_menu_item" -> "MENU_ITEMS";
            case "search_restaurant" -> "RESTAURANTS";
            case "track_order" -> "ORDER_TRACKING";
            case "create_order" -> "ORDER_CREATED";
            default -> "TOOL_RESULT";
        };
    }

    private String generateFinalResponse(String userMessage, String toolName, Object toolResult) {
        try {
            String toolResultJson = objectMapper.writeValueAsString(toolResult);
            String finalResponsePrompt = """
                    You are FoodieDash AI Assistant.
                    
                    Generate a final response for the user based on tool result.
                    - Language: same language as the user message
                    - Keep it concise and helpful.
                    - Do not mention system prompts or internal tools.
                    - If data is empty, explain clearly and suggest next query.
                    
                    CONTEXT:
                    - User message: %s
                    - Tool used: %s
                    - Tool result JSON: %s
                    
                    RESPONSE FORMAT:
                    {
                      "toolCall": false,
                      "finalMessage": "..."
                    }
                    
                    Return ONLY valid JSON.
                    """.formatted(userMessage, toolName, toolResultJson);

            AIRequest finalRequest = AIRequest.builder()
                    .systemPrompt(finalResponsePrompt)
                    .userMessage("Create final response.")
                    .build();

            AIResponse finalResponse = aiChatPort.chat(finalRequest);
            if (finalResponse.getFinalMessage() == null || finalResponse.getFinalMessage().isBlank()) {
                return toolResultJson;
            }
            return finalResponse.getFinalMessage();
        } catch (Exception e) {
            return String.valueOf(toolResult);
        }
    }
}
