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
        ca.isDefault = false;

        return ca;
    }

    public void update(CustomerAddress updated) {
        if (updated.getLabel() != null) {
            this.label = updated.getLabel();
        }
        if (updated.getAddress() != null) {
            this.address = updated.getAddress();
        }
        if (updated.getLat() != null) {
            this.lat = updated.getLat();
        }
        if (updated.getLng() != null) {
            this.lng = updated.getLng();
        }
        if (updated.getReceiverName() != null) {
            this.receiverName = updated.getReceiverName();
        }
        if (updated.getReceiverPhone() != null) {
            this.receiverPhone = updated.getReceiverPhone();
        }
        if (updated.getNote() != null) {
            this.note = updated.getNote();
        }
        this.isDefault = updated.isDefault();
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

