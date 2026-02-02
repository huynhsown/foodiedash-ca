package com.ute.foodiedash.application.ai.usecase;

import com.ute.foodiedash.application.ai.dto.AIChatResponseDTO;
import com.ute.foodiedash.application.ai.orchestrator.AIOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AIChatUseCase {
    private final AIOrchestrator orchestrator;

    public AIChatResponseDTO execute(String message) {

        return orchestrator.process(message);
    }

}
