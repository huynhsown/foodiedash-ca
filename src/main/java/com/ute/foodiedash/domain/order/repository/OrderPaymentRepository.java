package com.ute.foodiedash.domain.order.repository;

import com.ute.foodiedash.domain.order.model.OrderPayment;

import java.util.Optional;

public interface OrderPaymentRepository {
    OrderPayment save(OrderPayment orderPayment);

    Optional<OrderPayment> findByOrderId(Long orderId);
}
