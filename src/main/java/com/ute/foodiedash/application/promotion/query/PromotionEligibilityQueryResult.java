package com.ute.foodiedash.application.promotion.query;

public record PromotionEligibilityQueryResult(
    boolean eligible,
    Long promotionId,
    String reason
) {
    public static PromotionEligibilityQueryResult ok(Long promotionId) {
        return new PromotionEligibilityQueryResult(true, promotionId, null);
    }

    public static PromotionEligibilityQueryResult fail(String reason) {
        return new PromotionEligibilityQueryResult(false, null, reason);
    }
}
