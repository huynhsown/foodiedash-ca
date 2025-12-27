package com.ute.foodiedash.domain.order.repository;

import com.ute.foodiedash.domain.order.model.OrderPayment;

public interface OrderPaymentRepository {
    OrderPayment save(OrderPayment orderPayment);
}
