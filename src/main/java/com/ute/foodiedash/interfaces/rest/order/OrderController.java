package com.ute.foodiedash.interfaces.rest.order;

import com.ute.foodiedash.application.order.command.CheckoutOrderCommand;
import com.ute.foodiedash.application.order.query.CheckoutOrderResult;
import com.ute.foodiedash.application.order.usecase.CheckoutOrderUseCase;
import com.ute.foodiedash.interfaces.rest.order.dto.CheckoutOrderRequestDTO;
import com.ute.foodiedash.interfaces.rest.order.dto.CheckoutOrderResponseDTO;
import com.ute.foodiedash.interfaces.rest.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CheckoutOrderUseCase checkoutOrderUseCase;

    private final OrderMapper orderMapper;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutOrderResponseDTO> checkout(@RequestBody CheckoutOrderRequestDTO dto) {
        CheckoutOrderCommand command = orderMapper.toCommand(dto);
        CheckoutOrderResult result = checkoutOrderUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toResponseDto(result));
    }
}
