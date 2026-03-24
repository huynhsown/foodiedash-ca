package com.ute.foodiedash.application.reviews.command;

public record UpdateReviewCommand(
        Long reviewId,
        Long customerId,
        Integer rating,
        String comment
) {
}
