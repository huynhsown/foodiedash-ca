package com.ute.foodiedash.application.user.query;

import com.ute.foodiedash.domain.user.model.CustomerAddress;

import java.math.BigDecimal;

public record CustomerAddressQueryResult(
        Long id,
        String label,
        String address,
        BigDecimal lat,
        BigDecimal lng,
        String receiverName,
        String receiverPhone,
        String note,
        boolean isDefault
) {
    public static CustomerAddressQueryResult from(CustomerAddress address) {
        return new CustomerAddressQueryResult(
                address.getId(),
                address.getLabel(),
                address.getAddress(),
                address.getLat(),
                address.getLng(),
                address.getReceiverName(),
                address.getReceiverPhone(),
                address.getNote(),
                address.isDefaultAddress()
        );
    }
}

