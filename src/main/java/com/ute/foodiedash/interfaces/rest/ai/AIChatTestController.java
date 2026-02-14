package com.ute.foodiedash.interfaces.rest.ai;

import com.ute.foodiedash.application.ai.dto.AIChatResponseDTO;
import com.ute.foodiedash.application.ai.usecase.AIChatUseCase;
import com.ute.foodiedash.interfaces.rest.ai.dto.AIChatTestRequestDTO;
import com.ute.foodiedash.interfaces.rest.ai.dto.AIChatTestResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/ai")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.internal-test-apis-enabled", havingValue = "true")
public class AIChatTestController {

    private final AIChatUseCase aiChatUseCase;

    @PostMapping("/chat")
    public ResponseEntity<AIChatTestResponseDTO> chat(@Valid @RequestBody AIChatTestRequestDTO request) {
        AIChatResponseDTO result = aiChatUseCase.execute(request.message());
        return ResponseEntity.ok(new AIChatTestResponseDTO(
                result.getMessage(),
                result.getType(),
                result.getData()
        ));
    }
}
