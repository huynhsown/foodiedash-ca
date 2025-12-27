package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.application.order.query.OrderSummariesQueryResult;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOrdersByCustomerUseCase {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderSummariesQueryResult execute(Long customerId) {
        List<Order> orders = orderRepository.findSummariesByCustomerId(customerId);

        List<OrderSummaryQueryResult> results = orders.stream()
                .map(this::toOrderSummary)
                .filter(java.util.Objects::nonNull)
                .toList();

        return new OrderSummariesQueryResult(results);
    }

    private OrderSummaryQueryResult toOrderSummary(Order order) {
        if (order == null) {
            return null;
        }

        return new OrderSummaryQueryResult(
                order.getId(),
                order.getCode(),
                order.getStatus() != null ? order.getStatus().name() : null,
                order.getSubtotalAmount(),
                order.getDiscountAmount(),
                order.getDeliveryFee(),
                order.getTotalAmount(),
                order.getPlacedAt(),
                order.getAcceptedAt(),
                order.getPreparedAt(),
                order.getCancelledAt()
        );
    }
}

