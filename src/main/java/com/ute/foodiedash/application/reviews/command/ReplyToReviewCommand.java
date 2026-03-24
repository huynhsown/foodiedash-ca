package com.ute.foodiedash.application.reviews.command;

public record ReplyToReviewCommand(
        Long reviewId,
        Long merchantId,
        String reply
) {
}
