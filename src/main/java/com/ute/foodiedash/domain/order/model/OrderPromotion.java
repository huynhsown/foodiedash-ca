package com.ute.foodiedash.domain.order.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderPromotion extends BaseEntity {

    private Long id;
    private Long orderId;
    private Long promotionId;
    private String promotionCode;
    private BigDecimal discountAmount;

    public static OrderPromotion create(
            Long orderId,
            Long promotionId,
            String promotionCode,
            BigDecimal discountAmount
    ) {

        if (orderId == null) {
            throw new BadRequestException("Order id required");
        }

        if (promotionId == null) {
            throw new BadRequestException("Promotion id required");
        }

        if (promotionCode == null || promotionCode.isBlank()) {
            throw new BadRequestException("Promotion code required");
        }

        if (discountAmount == null) {
            throw new BadRequestException("Discount required");
        }

        if (discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Discount must be positive");
        }

        OrderPromotion promotion = new OrderPromotion();
        promotion.orderId = orderId;
        promotion.promotionId = promotionId;
        promotion.promotionCode = promotionCode;
        promotion.discountAmount = discountAmount;

        return promotion;
    }

    public static OrderPromotion reconstruct(
            Long id,
            Long orderId,
            Long promotionId,
            String promotionCode,
            BigDecimal discountAmount
    ) {

        OrderPromotion promotion = new OrderPromotion();
        promotion.id = id;
        promotion.orderId = orderId;
        promotion.promotionId = promotionId;
        promotion.promotionCode = promotionCode;
        promotion.discountAmount = discountAmount;

        return promotion;
    }
}
