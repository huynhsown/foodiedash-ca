package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.OrderPromotion;

import java.math.BigDecimal;
import java.util.List;

public record PromotionResult(
        Long promotionId,
        String promotionCode,
        BigDecimal discountAmount
) {
    public static PromotionResult from(OrderPromotion p) {
        return new PromotionResult(
                p.getPromotionId(),
                p.getPromotionCode(),
                p.getDiscountAmount()
        );
    }

    public static List<PromotionResult> listFrom(List<OrderPromotion> promotions) {
        if (promotions == null) {
            return List.of();
        }
        return promotions.stream()
                .map(PromotionResult::from)
                .toList();
    }
}
