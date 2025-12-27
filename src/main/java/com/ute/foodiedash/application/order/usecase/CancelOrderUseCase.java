package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.command.CancelOrderCommand;
import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.order.enums.OrderStatus;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderStatusHistory;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CancelOrderUseCase {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderSummaryQueryResult execute(Long customerId, CancelOrderCommand command) {
        if (customerId == null) {
            throw new BadRequestException("Customer id required");
        }
        if (command == null || command.orderId() == null) {
            throw new BadRequestException("Order id required");
        }
        if (command.reason() == null || command.reason().isBlank()) {
            throw new BadRequestException("Reason required");
        }

        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!java.util.Objects.equals(order.getCustomerId(), customerId)) {
            throw new ForbiddenException("You are not allowed to cancel this order");
        }

        OrderStatusHistory history = OrderStatusHistory.create(
                order.getId(),
                OrderStatus.CANCELLED,
                command.reason()
        );

        order.cancel(command.reason());
        order.addStatusHistory(history);

        Order saved = orderRepository.save(order);

        return new OrderSummaryQueryResult(
                saved.getId(),
                saved.getCode(),
                saved.getStatus() != null ? saved.getStatus().name() : null,
                saved.getSubtotalAmount(),
                saved.getDiscountAmount(),
                saved.getDeliveryFee(),
                saved.getTotalAmount(),
                saved.getPlacedAt(),
                saved.getAcceptedAt(),
                saved.getPreparedAt(),
                saved.getCancelledAt()
        );
    }
}

