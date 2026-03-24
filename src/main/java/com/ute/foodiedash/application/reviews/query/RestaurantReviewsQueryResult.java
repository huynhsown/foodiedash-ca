package com.ute.foodiedash.application.reviews.query;

import java.util.List;

public record RestaurantReviewsQueryResult(
    List<ReviewQueryResult> reviews,
    int totalCount
) {}
