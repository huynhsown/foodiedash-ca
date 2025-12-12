package com.ute.foodiedash.domain.order.enums;

public enum PaymentMethod {
    COD,
    STRIPE,
    VNPAY;

    public boolean isOnline() {
        return this == STRIPE || this == VNPAY;
    }

    public boolean isOffline() {
        return this == COD;
    }
}
