package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.driver.port.DriverBusyStatePort;
import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.repository.OrderDeliveryRepository;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompleteOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final DriverBusyStatePort driverBusyStatePort;

    @Transactional
    public OrderSummaryQueryResult execute(Long customerId, Long orderId) {
        if (customerId == null) {
            throw new BadRequestException("Customer id required");
        }
        if (orderId == null) {
            throw new BadRequestException("Order id required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!Objects.equals(order.getCustomerId(), customerId)) {
            throw new ForbiddenException("You are not allowed to complete this order");
        }

        order.markCompleted();
        Order saved = orderRepository.save(order);

        Optional<OrderDelivery> deliveryOpt = orderDeliveryRepository.findByOrderId(orderId);
        deliveryOpt.map(OrderDelivery::getDriverId)
                .filter(Objects::nonNull)
                .ifPresent(driverBusyStatePort::clear);

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
