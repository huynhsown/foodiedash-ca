package com.ute.foodiedash.application.order.port;

import com.ute.foodiedash.application.order.query.VoucherLockResult;

import java.math.BigDecimal;

public interface PromotionLockPort {
    VoucherLockResult holdPromotion(String code, Long customerId, BigDecimal subtotal);
}
