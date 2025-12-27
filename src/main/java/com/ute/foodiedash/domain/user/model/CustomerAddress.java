package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;

@Getter
public class CustomerAddress extends BaseEntity {

    private Long id;
    private Long userId;

    private String label;
    private String address;
    private BigDecimal lat;
    private BigDecimal lng;

    private String receiverName;
    private String receiverPhone;
    private String note;

    private boolean defaultAddress;

    private CustomerAddress() {}

    public static CustomerAddress create(
            String label,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String receiverName,
            String receiverPhone,
            String note
    ) {
        if (label == null || label.isBlank()) {
            throw new BadRequestException("Address label is required");
        }

        if (address == null || address.isBlank()) {
            throw new BadRequestException("Address is required");
        }

        if (lat == null || lng == null) {
            throw new BadRequestException("Address location (latitude and longitude) is required");
        }

        CustomerAddress ca = new CustomerAddress();
        ca.label = label;
        ca.address = address;
        ca.lat = lat;
        ca.lng = lng;
        ca.receiverName = receiverName;
        ca.receiverPhone = receiverPhone;
        ca.note = note;
        ca.defaultAddress = false;

        return ca;
    }

    public static CustomerAddress reconstruct(
            Long id,
            Long userId,
            String label,
            String address,
            BigDecimal lat,
            BigDecimal lng,
            String receiverName,
            String receiverPhone,
            String note,
            boolean defaultAddress,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        CustomerAddress ca = new CustomerAddress();
        ca.id = id;
        ca.userId = userId;
        ca.label = label;
        ca.address = address;
        ca.lat = lat;
        ca.lng = lng;
        ca.receiverName = receiverName;
        ca.receiverPhone = receiverPhone;
        ca.note = note;
        ca.defaultAddress = defaultAddress;
        ca.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
        return ca;
    }

    public void attachToUser(Long userId) {
        this.userId = userId;
    }

    public void update(CustomerAddress updated) {
        if (updated.label != null) {
            this.label = updated.label;
        }
        if (updated.address != null) {
            this.address = updated.address;
        }
        if (updated.lat != null) {
            this.lat = updated.lat;
        }
        if (updated.lng != null) {
            this.lng = updated.lng;
        }
        if (updated.receiverName != null) {
            this.receiverName = updated.receiverName;
        }
        if (updated.receiverPhone != null) {
            this.receiverPhone = updated.receiverPhone;
        }
        if (updated.note != null) {
            this.note = updated.note;
        }
        this.defaultAddress = updated.isDefaultAddress();
    }

    public void setAsDefault() {
        this.defaultAddress = true;
    }

    public void unsetDefault() {
        this.defaultAddress = false;
    }

    public boolean belongsToUser(Long userId) {
        return this.userId != null && this.userId.equals(userId);
    }
}

