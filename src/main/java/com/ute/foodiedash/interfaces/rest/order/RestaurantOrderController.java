package com.ute.foodiedash.interfaces.rest.order;

import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.application.order.usecase.AcceptOrderUseCase;
import com.ute.foodiedash.application.order.usecase.MarkReadyOrderUseCase;
import com.ute.foodiedash.application.order.usecase.PrepareOrderUseCase;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderSummaryResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderSummaryDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurant/orders")
@RequiredArgsConstructor
public class RestaurantOrderController {

    private final AcceptOrderUseCase acceptOrderUseCase;
    private final PrepareOrderUseCase prepareOrderUseCase;
    private final MarkReadyOrderUseCase markReadyOrderUseCase;
    private final OrderSummaryDtoMapper orderSummaryDtoMapper;

    private Long getCurrentMerchantId() {
        try {
            return SecurityContextHelper.getCurrentUserId();
        } catch (UnauthorizedException e) {
            // Keep flow usable when auth is disabled in local/dev.
            return 1L;
        }
    }

    @PostMapping("/{orderId}/accept")
    public ResponseEntity<OrderSummaryResponseDTO> acceptOrder(@PathVariable Long orderId) {
        OrderSummaryQueryResult result = acceptOrderUseCase.execute(getCurrentMerchantId(), orderId);
        return ResponseEntity.ok(orderSummaryDtoMapper.toResponseDto(result));
    }

    @PostMapping("/{orderId}/prepare")
    public ResponseEntity<OrderSummaryResponseDTO> prepareOrder(@PathVariable Long orderId) {
        OrderSummaryQueryResult result = prepareOrderUseCase.execute(getCurrentMerchantId(), orderId);
        return ResponseEntity.ok(orderSummaryDtoMapper.toResponseDto(result));
    }

    @PostMapping("/{orderId}/ready")
    public ResponseEntity<OrderSummaryResponseDTO> markReady(@PathVariable Long orderId) {
        OrderSummaryQueryResult result = markReadyOrderUseCase.execute(getCurrentMerchantId(), orderId);
        return ResponseEntity.ok(orderSummaryDtoMapper.toResponseDto(result));
    }
}

