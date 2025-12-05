package com.ute.foodiedash.application.promotion.query;

import java.util.List;

public record PromotionPageResult(
    List<PromotionQueryResult> content,
    int number,
    int size,
    long totalElements,
    int totalPages,
    boolean empty,
    int numberOfElements,
    boolean hasNextPage,
    boolean hasPreviousPage
) {}
