package com.ute.foodiedash.interfaces.rest.order;

import com.ute.foodiedash.application.order.usecase.AutoAssignDriverOnReadyUseCase;
import com.ute.foodiedash.interfaces.rest.order.dto.AutoAssignDriverTestResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gọi trực tiếp {@link AutoAssignDriverOnReadyUseCase} để test không cần bắn event mark-ready.
 * Tắt production: {@code app.internal-test-apis-enabled=false}
 */
@RestController
@RequestMapping("/api/v1/internal/orders")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.internal-test-apis-enabled", havingValue = "true")
public class OrderDispatchTestController {

    private final AutoAssignDriverOnReadyUseCase autoAssignDriverOnReadyUseCase;

    @PostMapping("/{orderId}/auto-assign-driver")
    public ResponseEntity<AutoAssignDriverTestResponseDTO> triggerAutoAssignDriver(@PathVariable Long orderId) {
        autoAssignDriverOnReadyUseCase.execute(orderId);
        return ResponseEntity.ok(new AutoAssignDriverTestResponseDTO(
                orderId,
                "AutoAssignDriverOnReadyUseCase.execute() finished — check logs and DB for READY/DELIVERING."
        ));
    }
}
