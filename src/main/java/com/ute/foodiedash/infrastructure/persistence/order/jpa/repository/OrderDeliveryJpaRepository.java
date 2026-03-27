package com.ute.foodiedash.infrastructure.persistence.order.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderDeliveryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDeliveryJpaRepository extends JpaRepository<OrderDeliveryJpaEntity, Long> {
    @Query("""
        SELECT od FROM OrderDeliveryJpaEntity od
        WHERE od.orderId = :orderId
        AND od.deletedAt IS NULL
    """)
    Optional<OrderDeliveryJpaEntity> findByOrderId(Long orderId);
}
