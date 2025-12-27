package com.ute.foodiedash.interfaces.rest.order;

import com.ute.foodiedash.application.order.command.CheckoutOrderCommand;
import com.ute.foodiedash.application.order.command.CancelOrderCommand;
import com.ute.foodiedash.application.order.command.PreviewOrderCommand;
import com.ute.foodiedash.application.order.query.CheckoutOrderResult;
import com.ute.foodiedash.application.order.query.OrderDetailQueryResult;
import com.ute.foodiedash.application.order.query.OrderSummariesQueryResult;
import com.ute.foodiedash.application.order.usecase.CancelOrderUseCase;
import com.ute.foodiedash.application.order.query.PreviewOrderResult;
import com.ute.foodiedash.application.order.usecase.CheckoutOrderUseCase;
import com.ute.foodiedash.application.order.usecase.GetOrderDetailUseCase;
import com.ute.foodiedash.application.order.usecase.GetOrdersByCustomerUseCase;
import com.ute.foodiedash.application.order.usecase.PreviewOrderUseCase;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import com.ute.foodiedash.interfaces.rest.order.dto.CheckoutOrderRequestDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.CheckoutOrderResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.CancelOrderRequestDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderDetailResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.OrderSummariesResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PreviewOrderRequestDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.PreviewOrderResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderDetailDtoMapper;
import com.ute.foodiedash.interfaces.rest.order.mapper.CancelOrderDtoMapper;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderMapper;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderSummaryDtoMapper;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CheckoutOrderUseCase checkoutOrderUseCase;
    private final PreviewOrderUseCase previewOrderUseCase;
    private final GetOrderDetailUseCase getOrderDetailUseCase;
    private final GetOrdersByCustomerUseCase getOrdersByCustomerUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    private final OrderMapper orderMapper;
    private final OrderDetailDtoMapper orderDetailDtoMapper;
    private final OrderSummaryDtoMapper orderSummaryDtoMapper;
    private final CancelOrderDtoMapper cancelOrderDtoMapper;

    private Long getCurrentCustomerId() {
        try {
            return SecurityContextHelper.getCurrentUserId();
        } catch (UnauthorizedException e) {
            return 1L;
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutOrderResponseDTO> checkout(@RequestBody CheckoutOrderRequestDTO dto) {
        CheckoutOrderCommand command = orderMapper.toCommand(dto);
        CheckoutOrderResult result = checkoutOrderUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toResponseDto(result));
    }

    @PostMapping("/preview")
    public ResponseEntity<PreviewOrderResponseDTO> preview(@RequestBody PreviewOrderRequestDTO dto) {
        PreviewOrderCommand command = orderMapper.toPreviewCommand(dto);
        PreviewOrderResult result = previewOrderUseCase.execute(command);
        return ResponseEntity.ok(orderMapper.toPreviewResponseDto(result));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDTO> getOrderDetail(@PathVariable Long orderId) {
        OrderDetailQueryResult result = getOrderDetailUseCase.execute(getCurrentCustomerId(), orderId);
        return ResponseEntity.ok(orderDetailDtoMapper.toResponseDto(result));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody CancelOrderRequestDTO dto) {
        CancelOrderCommand command = cancelOrderDtoMapper.toCommand(orderId, dto);
        var result = cancelOrderUseCase.execute(getCurrentCustomerId(), command);
        return ResponseEntity.ok(orderSummaryDtoMapper.toResponseDto(result));
    }

    @GetMapping
    public ResponseEntity<OrderSummariesResponseDTO> getOrdersOfCurrentCustomer() {
        Long customerId = getCurrentCustomerId();
        OrderSummariesQueryResult result = getOrdersByCustomerUseCase.execute(customerId);
        return ResponseEntity.ok(orderSummaryDtoMapper.toResponseDto(result));
    }
}
