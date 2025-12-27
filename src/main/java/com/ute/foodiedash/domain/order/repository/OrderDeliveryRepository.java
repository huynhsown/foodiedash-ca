package com.ute.foodiedash.domain.order.repository;

import com.ute.foodiedash.domain.order.model.OrderDelivery;

import java.util.Optional;

public interface OrderDeliveryRepository {
    OrderDelivery save(OrderDelivery orderDelivery);

    Optional<OrderDelivery> findByOrderId(Long orderId);
}
