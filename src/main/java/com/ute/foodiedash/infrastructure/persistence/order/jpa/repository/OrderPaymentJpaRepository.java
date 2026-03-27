package com.ute.foodiedash.infrastructure.persistence.order.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderPaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPaymentJpaRepository extends JpaRepository<OrderPaymentJpaEntity, Long> {
}
