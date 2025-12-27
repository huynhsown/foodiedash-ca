package com.ute.foodiedash.infrastructure.persistence.order.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderPaymentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderPaymentJpaRepository extends JpaRepository<OrderPaymentJpaEntity, Long> {

    Optional<OrderPaymentJpaEntity> findByOrderId(Long orderId);
}
