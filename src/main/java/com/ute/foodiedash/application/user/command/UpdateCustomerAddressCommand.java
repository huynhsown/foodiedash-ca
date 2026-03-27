package com.ute.foodiedash.application.user.command;

import java.math.BigDecimal;

public record UpdateCustomerAddressCommand(
        Long addressId,
        String label,
        String address,
        BigDecimal lat,
        BigDecimal lng,
        String receiverName,
        String receiverPhone,
        String note,
        boolean defaultAddress
) {
}

