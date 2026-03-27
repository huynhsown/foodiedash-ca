package com.ute.foodiedash.domain.order.repository;

import com.ute.foodiedash.domain.order.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long id);

    Optional<Order> findByCode(String code);

    List<Order> findSummariesByCustomerId(Long customerId);

    void softDeleteById(Long id);

    void restoreById(Long id);
}
