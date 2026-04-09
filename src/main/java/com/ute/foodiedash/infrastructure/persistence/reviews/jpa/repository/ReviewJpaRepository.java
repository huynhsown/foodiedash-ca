package com.ute.foodiedash.infrastructure.persistence.reviews.jpa.repository;

import com.ute.foodiedash.domain.reviews.enums.ReviewStatus;
import com.ute.foodiedash.infrastructure.persistence.reviews.jpa.entity.ReviewJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, Long> {

    Optional<ReviewJpaEntity> findByIdAndDeletedAtIsNullAndStatus(Long id, ReviewStatus status);

    List<ReviewJpaEntity> findByCustomerIdOrderByCreatedAtDesc(Long customerId, Pageable pageable);

    List<ReviewJpaEntity> findByOrderIdOrderByCreatedAtDesc(Long orderId, Pageable pageable);

    List<ReviewJpaEntity> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId, Pageable pageable);

    List<ReviewJpaEntity> findByRestaurantIdAndDeletedAtIsNullAndStatusOrderByCreatedAtDesc(
            Long restaurantId, ReviewStatus status, Pageable pageable);

    int countByCustomerId(Long customerId);

    int countByRestaurantId(Long restaurantId);

    int countByRestaurantIdAndDeletedAtIsNullAndStatus(Long restaurantId, ReviewStatus status);

    long countByOrderId(Long orderId);

    boolean existsByOrderIdAndCustomerId(Long orderId, Long customerId);

    Optional<ReviewJpaEntity> findByOrderIdAndCustomerId(Long orderId, Long customerId);
}
