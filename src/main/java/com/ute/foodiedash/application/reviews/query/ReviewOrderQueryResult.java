package com.ute.foodiedash.application.reviews.query;

import java.util.List;

public record ReviewOrderQueryResult(
    Long orderId,
    List<ReviewOrderItemQueryResult> items
) {}
