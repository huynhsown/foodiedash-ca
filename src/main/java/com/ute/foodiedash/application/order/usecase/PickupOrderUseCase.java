package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.query.PickupOrderResult;
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

@Component
@RequiredArgsConstructor
public class PickupOrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    @Transactional
    public PickupOrderResult execute(Long driverId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        OrderDelivery orderDelivery = orderDeliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BadRequestException("No delivery information found for this order"));

        if (!orderDelivery.isAssignedToDriver(driverId)) {
            throw new ForbiddenException("Driver is not assigned to this order");
        }

        OrderPayment orderPayment = orderPaymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BadRequestException("Order payment not found"));

        orderDelivery.markPickedUp();
        order.startDelivery("Driver has picked up the order");

        OrderDelivery savedDelivery = orderDeliveryRepository.save(orderDelivery);
        Order savedOrder = orderRepository.save(order);

        return PickupOrderResult.from(savedOrder, savedDelivery, orderPayment);
    }
}
