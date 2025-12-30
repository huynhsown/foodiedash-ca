package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.query.OrderDetailQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
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
public class GetOrderDetailUseCase {
    private final OrderRepository orderRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;

    @Transactional(readOnly = true)
    public OrderDetailQueryResult execute(Long customerId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!Objects.equals(order.getCustomerId(), customerId)) {
            throw new UnauthorizedException("You are not allowed to access this order");
        }

        OrderPayment payment = orderPaymentRepository.findByOrderId(orderId).orElse(null);
        OrderDelivery delivery = orderDeliveryRepository.findByOrderId(orderId).orElse(null);

        return OrderDetailQueryResult.from(order, payment, delivery);
    }
}
