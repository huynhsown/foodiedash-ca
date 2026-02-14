package com.ute.foodiedash.application.ai.port;

import com.ute.foodiedash.application.ai.orchestrator.model.AIRequest;
import com.ute.foodiedash.application.ai.orchestrator.model.AIResponse;

public interface AIChatPort {
    AIResponse chat(AIRequest request);
}
