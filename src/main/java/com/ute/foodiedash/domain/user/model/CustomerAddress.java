package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    private boolean isDefault;

    public static CustomerAddress create(
        Long userId,
        String label,
        String address,
        BigDecimal lat,
        BigDecimal lng,
        String receiverName,
        String receiverPhone,
        String note
    ) {
        if (userId == null) {
            throw new BadRequestException("USER_ID_REQUIRED");
        }
        if (label == null || label.isBlank()) {
            throw new BadRequestException("ADDRESS_LABEL_REQUIRED");
        }
        if (address == null || address.isBlank()) {
            throw new BadRequestException("ADDRESS_REQUIRED");
        }
        if (lat == null || lng == null) {
            throw new BadRequestException("ADDRESS_LOCATION_REQUIRED");
        }

        CustomerAddress ca = new CustomerAddress();
        ca.userId = userId;
        ca.label = label;
        ca.address = address;
        ca.lat = lat;
        ca.lng = lng;
        ca.receiverName = receiverName;
        ca.receiverPhone = receiverPhone;
        ca.note = note;
        ca.isDefault = false; // match DB default
        return ca;
    }

    public void update(
        String label,
        String address,
        BigDecimal lat,
        BigDecimal lng,
        String receiverName,
        String receiverPhone,
        String note
    ) {
        if (label != null && !label.isBlank()) {
            this.label = label;
        }
        if (address != null && !address.isBlank()) {
            this.address = address;
        }
        if (lat != null) {
            this.lat = lat;
        }
        if (lng != null) {
            this.lng = lng;
        }
        if (receiverName != null) {
            this.receiverName = receiverName;
        }
        if (receiverPhone != null) {
            this.receiverPhone = receiverPhone;
        }
        if (note != null) {
            this.note = note;
        }
    }

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void unsetDefault() {
        this.isDefault = false;
    }

    public boolean belongsToUser(Long userId) {
        return this.userId != null && this.userId.equals(userId);
    }
}

