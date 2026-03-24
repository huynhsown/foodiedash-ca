package com.ute.foodiedash.infrastructure.persistence.reviews.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.reviews.jpa.entity.ReviewJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, Long> {

    List<ReviewJpaEntity> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);

    List<ReviewJpaEntity> findByOrderIdOrderByCreatedAtDesc(Long orderId, Pageable pageable);

    List<ReviewJpaEntity> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);

    int countByCustomerId(Long customerId);

    int countByRestaurantId(Long restaurantId);

    long countByOrderId(Long orderId);

    boolean existsByOrderIdAndCustomerId(Long orderId, Long customerId);
}
