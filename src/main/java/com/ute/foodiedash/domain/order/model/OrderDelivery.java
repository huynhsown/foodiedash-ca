package com.ute.foodiedash.domain.order.model;

import com.ute.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class OrderDelivery extends BaseEntity {
    private Long id;
    private Long orderId;
    private Long driverId;
    private String address;
    private BigDecimal lat;
    private BigDecimal lng;
    private String receiverName;
    private String receiverPhone;
    private String note;
    private BigDecimal distanceKm;
    private Integer etaMinutes;
    private String geometry;
    private Instant pickedUpAt;
    private Instant deliveredAt;

    public static OrderDelivery create(
            Long orderId,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String receiverName,
            String receiverPhone,
            String note
    ) {

        if (orderId == null) {
            throw new BadRequestException("Order id required");
        }

        if (address == null || address.isBlank()) {
            throw new BadRequestException("Address required");
        }

        if (lat == null || lng == null) {
            throw new BadRequestException("Location required");
        }

        if (receiverName == null || receiverName.isBlank()) {
            throw new BadRequestException("Receiver name required");
        }

        if (receiverPhone == null || receiverPhone.isBlank()) {
            throw new BadRequestException("Receiver phone required");
        }

        OrderDelivery delivery = new OrderDelivery();

        delivery.orderId = orderId;
        delivery.address = address;
        delivery.lat = lat;
        delivery.lng = lng;
        delivery.receiverName = receiverName;
        delivery.receiverPhone = receiverPhone;
        delivery.note = note;

        return delivery;
    }

    public static OrderDelivery reconstruct(
            Long id,
            Long orderId,
            Long driverId,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String receiverName,
            String receiverPhone,
            String note,
            BigDecimal distanceKm,
            Integer etaMinutes,
            String geometry,
            Instant pickedUpAt,
            Instant deliveredAt
    ) {

        OrderDelivery delivery = new OrderDelivery();

        delivery.id = id;
        delivery.orderId = orderId;
        delivery.driverId = driverId;
        delivery.address = address;
        delivery.lat = lat;
        delivery.lng = lng;
        delivery.receiverName = receiverName;
        delivery.receiverPhone = receiverPhone;
        delivery.note = note;
        delivery.distanceKm = distanceKm;
        delivery.etaMinutes = etaMinutes;
        delivery.geometry = geometry;
        delivery.pickedUpAt = pickedUpAt;
        delivery.deliveredAt = deliveredAt;

        return delivery;
    }

    public void assignDriver(Long driverId) {
        if (this.driverId != null) {
            throw new BadRequestException("Driver already assigned");
        }
        if (driverId == null) {
            throw new BadRequestException("Driver required");
        }
        this.driverId = driverId;
    }

    public void unassignDriver() {
        if (this.driverId == null) {
            throw new BadRequestException("Driver not assigned");
        }

        if (this.pickedUpAt != null) {
            throw new BadRequestException("Order already picked up");
        }

        this.driverId = null;
    }

    public void updateRouteInfo(BigDecimal distanceKm, Integer etaMinutes, String geometry) {

        if (distanceKm == null) {
            throw new BadRequestException("Distance required");
        }

        if (distanceKm.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Distance must be positive");
        }

        if (etaMinutes == null) {
            throw new BadRequestException("ETA required");
        }

        if (etaMinutes < 0) {
            throw new BadRequestException("ETA must be positive");
        }

        if (geometry == null || geometry.isBlank()) {
            throw new BadRequestException("Geometry is required");
        }

        this.distanceKm = distanceKm;
        this.etaMinutes = etaMinutes;
        this.geometry = geometry;
    }

    public void markPickedUp() {
        if (driverId == null) {
            throw new BadRequestException("Driver not assigned");
        }

        if (pickedUpAt != null) {
            throw new BadRequestException("Already picked up");
        }
        this.pickedUpAt = Instant.now();
    }

    public void markDelivered() {

        if (driverId == null) {
            throw new BadRequestException("Driver not assigned");
        }

        if (pickedUpAt == null) {
            throw new BadRequestException("Order not picked up yet");
        }

        if (deliveredAt != null) {
            throw new BadRequestException("Already delivered");
        }

        this.deliveredAt = Instant.now();
    }
}
