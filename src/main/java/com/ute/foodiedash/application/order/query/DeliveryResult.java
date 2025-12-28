package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.OrderDelivery;

import java.math.BigDecimal;
import java.util.List;

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
    public static DeliveryResult from(OrderDelivery delivery) {
        if (delivery == null) {
            return null;
        }
        return new DeliveryResult(
                delivery.getAddress(),
                delivery.getLat(),
                delivery.getLng(),
                delivery.getReceiverName(),
                delivery.getReceiverPhone(),
                delivery.getNote(),
                delivery.getDistanceKm(),
                delivery.getEtaMinutes(),
                CoordinateResult.listFrom(delivery.getGeometry())
        );
    }
}
