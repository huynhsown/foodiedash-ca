package com.ute.foodiedash.interfaces.rest.order;

import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.application.order.usecase.AcceptOrderUseCase;
import com.ute.foodiedash.application.order.usecase.MarkReadyOrderUseCase;
import com.ute.foodiedash.application.order.usecase.PrepareOrderUseCase;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderSummaryResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderSummaryDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurant/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MERCHANT')")
public class RestaurantOrderController {

    private final AcceptOrderUseCase acceptOrderUseCase;
    private final PrepareOrderUseCase prepareOrderUseCase;
    private final MarkReadyOrderUseCase markReadyOrderUseCase;
    private final OrderSummaryDtoMapper orderSummaryDtoMapper;

    private Long getCurrentMerchantId() {
        return SecurityContextHelper.getCurrentUserId();
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

