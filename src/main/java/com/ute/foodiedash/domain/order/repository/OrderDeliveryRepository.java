package com.ute.foodiedash.domain.order.repository;

import com.ute.foodiedash.domain.order.model.OrderDelivery;

public interface OrderDeliveryRepository {
    OrderDelivery save(OrderDelivery orderDelivery);
}
