package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.application.paymentmethod.query.PaymentMethodQueryResult;
import com.ute.foodiedash.application.promotion.query.PromotionPreviewResult;

import java.math.BigDecimal;
import java.util.List;

public record PreviewOrderResult(
        List<PreviewOrderItemResult> items,
        PromotionPreviewResult promotion,
        String paymentMethod,
        PriceBreakdownResult priceBreakdown,
        BigDecimal total,
        BigDecimal distanceMeters,
        Integer etaInMinutes,
        boolean canCheckout,
        List<PaymentMethodQueryResult> paymentMethods
) {}


