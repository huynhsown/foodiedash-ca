package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.driver.port.DriverBusyStatePort;
import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.application.order.port.OrderCustomerNotificationPort;
import com.ute.foodiedash.domain.notification.enums.NotificationType;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.model.OrderPayment;
import com.ute.foodiedash.domain.order.repository.OrderDeliveryRepository;
import com.ute.foodiedash.domain.order.repository.OrderPaymentRepository;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CompleteOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final DriverBusyStatePort driverBusyStatePort;
    private final OrderCustomerNotificationPort orderCustomerNotificationPort;

    @Transactional
    public OrderSummaryQueryResult execute(Long driverId, Long orderId) {
        if (driverId == null) {
            throw new BadRequestException("Driver id required");
        }
        if (orderId == null) {
            throw new BadRequestException("Order id required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        OrderDelivery orderDelivery = orderDeliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BadRequestException("No delivery information found for this order"));
        OrderPayment orderPayment = orderPaymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BadRequestException("Order payment not found"));

        if (!Objects.equals(orderDelivery.getDriverId(), driverId)) {
            throw new ForbiddenException("You are not allowed to complete this order");
        }

        orderDelivery.markDelivered();
        order.markCompleted("Driver delivered successfully");
        orderPayment.ensurePaidWhenOrderCompleted(order.getCode());

        Order saved = orderRepository.save(order);
        orderDeliveryRepository.save(orderDelivery);
        orderPaymentRepository.save(orderPayment);

        driverBusyStatePort.clear(driverId);
        orderCustomerNotificationPort.notifyForOrderStatus(saved, NotificationType.ORDER_COMPLETED);

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
                saved.getCancelledAt(),
                saved.getCompleteAt(),
                null,
                null,
                null
        );
    }
}
