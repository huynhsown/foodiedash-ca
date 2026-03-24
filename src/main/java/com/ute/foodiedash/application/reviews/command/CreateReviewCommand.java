package com.ute.foodiedash.application.reviews.command;

public record CreateReviewCommand(
        Long orderId,
        Long customerId,
        Integer rating,
        String comment
) {
}
