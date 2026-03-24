package com.ute.foodiedash.application.reviews.usecase;

import com.ute.foodiedash.application.reviews.command.ReplyToReviewCommand;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.domain.common.exception.ForbiddenException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.reviews.model.Review;
import com.ute.foodiedash.domain.reviews.repository.ReviewRepository;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReplyToReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewQueryResult execute(ReplyToReviewCommand command) {
        Review review = reviewRepository.findById(command.reviewId())
                .orElseThrow(() -> new NotFoundException("Review not found"));

        boolean canManageRestaurant = userRepository.existsMerchantRestaurant(
                command.merchantId(), review.getRestaurantId());
        if (!canManageRestaurant) {
            throw new ForbiddenException("You are not allowed to reply to this review");
        }

        review.reply(command.reply());

        review = reviewRepository.save(review);

        return ReviewQueryResult.from(review);
    }
}
