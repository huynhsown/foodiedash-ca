package com.ute.foodiedash.application.ai.orchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {
    private boolean toolCall;

    private String toolName;

    private String toolArguments;

    private String finalMessage;
}
