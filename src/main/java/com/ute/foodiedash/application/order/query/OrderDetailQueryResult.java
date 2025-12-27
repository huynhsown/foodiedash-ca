package com.ute.foodiedash.application.order.query;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailQueryResult(
        Long orderId,
        String orderCode,
        String status,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal deliveryFee,
        BigDecimal total,
        LocalDateTime placedAt,
        LocalDateTime acceptedAt,
        LocalDateTime preparedAt,
        LocalDateTime cancelledAt,
        List<ItemResult> items,
        List<PromotionResult> promotions,
        List<StatusHistoryResult> statusHistories,
        PaymentResult payment,
        DeliveryResult delivery
) {
    public record ItemResult(
            Long itemId,
            Long menuItemId,
            String name,
            String imageUrl,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            String notes,
            List<OptionResult> options
    ) {
    }

    public record OptionResult(
            Long optionId,
            Long sourceOptionId,
            String optionName,
            Boolean required,
            Integer minValue,
            Integer maxValue,
            List<OptionValueResult> values
    ) {
    }

    public record OptionValueResult(
            Long optionValueId,
            Long sourceOptionValueId,
            String optionValueName,
            Integer quantity,
            BigDecimal extraPrice
    ) {
    }

    public record PromotionResult(
            Long promotionId,
            String promotionCode,
            BigDecimal discountAmount
    ) {
    }

    public record StatusHistoryResult(
            Long statusHistoryId,
            String status,
            String note,
            Instant createdAt
    ) {
    }

    public record PaymentResult(
            String paymentMethodCode,
            String paymentStatus,
            String transactionId,
            Instant paidAt,
            Instant refundedAt
    ) {
    }

    public record DeliveryResult(
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String receiverName,
            String receiverPhone,
            String note,
            BigDecimal distanceKm,
            Integer etaMinutes,
            List<CoordinateResult> geometry
    ) {
    }

    public record CoordinateResult(
            BigDecimal lat,
            BigDecimal lng
    ) {
    }
}
