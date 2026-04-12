package com.ute.foodiedash.interfaces.rest.order;

import com.ute.foodiedash.application.order.query.PickupOrderResult;
import com.ute.foodiedash.application.order.usecase.PickupOrderUseCase;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.order.dto.PickupOrderResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.mapper.PickupOrderDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/driver/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ORDER_PICKUP')")
public class DriverOrderController {

    private final PickupOrderUseCase pickupOrderUseCase;
    private final PickupOrderDtoMapper pickupOrderDtoMapper;

    private Long getCurrentDriverId() {
        return SecurityContextHelper.getCurrentUserId();
    }

    @PostMapping("/{orderId}/pickup")
    public ResponseEntity<PickupOrderResponseDTO> pickupOrder(@PathVariable Long orderId) {
        PickupOrderResult result = pickupOrderUseCase.execute(getCurrentDriverId(), orderId);
        return ResponseEntity.ok(pickupOrderDtoMapper.toResponseDto(result));
    }
}
