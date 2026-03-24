package com.ute.foodiedash.application.reviews.query;

import com.ute.foodiedash.domain.reviews.model.Review;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record ReviewQueryResult(
        Long id,
        Long orderId,
        Long customerId,
        Long restaurantId,
        Integer rating,
        String comment,
        List<String> images,
        String merchantReply,
        LocalDateTime repliedAt,
        String status,
        Instant createdAt
) {
    public static ReviewQueryResult from(Review review) {
        return new ReviewQueryResult(
                review.getId(),
                review.getOrderId(),
                review.getCustomerId(),
                review.getRestaurantId(),
                review.getRating(),
                review.getComment(),
                review.getImages(),
                review.getMerchantReply(),
                review.getRepliedAt(),
                review.getStatus() != null ? review.getStatus().name() : null,
                review.getCreatedAt()
        );
    }
}
