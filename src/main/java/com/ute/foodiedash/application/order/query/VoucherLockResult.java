package com.ute.foodiedash.application.order.query;

public record VoucherLockResult(
        String lockId,
        boolean success,
        String reason
) {}
