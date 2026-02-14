package com.ute.foodiedash.application.ai.orchestrator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AIRequest {

    private String systemPrompt;

    private String userMessage;

}
