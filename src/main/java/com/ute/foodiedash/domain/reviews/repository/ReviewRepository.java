package com.ute.foodiedash.domain.reviews.repository;

import com.ute.foodiedash.domain.reviews.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);

    Optional<Review> findById(Long id);

    Optional<Review> findActiveById(Long id);

    List<Review> findByCustomerId(Long customerId, int limit, int offset);

    List<Review> findByOrderId(Long orderId, int limit, int offset);

    List<Review> findByRestaurantId(Long restaurantId, int limit, int offset);

    int countByCustomerId(Long customerId);

    int countByRestaurantId(Long restaurantId);

    long countByOrderId(Long orderId);

    boolean existsByOrderIdAndCustomerId(Long orderId, Long customerId);

    Optional<Review> findByOrderIdAndCustomerId(Long orderId, Long customerId);
}
