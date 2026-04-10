package com.ute.foodiedash.application.reviews.usecase;

import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.reviews.model.Review;
import com.ute.foodiedash.domain.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetOrderReviewUseCase {

    private final ReviewRepository reviewRepository;

    public ReviewQueryResult execute(Long orderId, Long customerId) {
        Review review = reviewRepository.findByOrderIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        return ReviewQueryResult.from(review);
    }
}
