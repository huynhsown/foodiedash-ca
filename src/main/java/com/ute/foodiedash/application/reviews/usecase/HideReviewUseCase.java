package com.ute.foodiedash.application.reviews.usecase;

import com.ute.foodiedash.application.reviews.command.HideReviewCommand;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.reviews.model.Review;
import com.ute.foodiedash.domain.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class HideReviewUseCase {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewQueryResult execute(HideReviewCommand command) {
        Review review = reviewRepository.findById(command.reviewId())
                .orElseThrow(() -> new NotFoundException("Review not found"));

        review.hide();

        review = reviewRepository.save(review);

        return ReviewQueryResult.from(review);
    }
}
