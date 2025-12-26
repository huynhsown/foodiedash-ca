package com.ute.foodiedash.domain.paymentmethod.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodType;
import lombok.Getter;

import java.time.Instant;

@Getter
public class PaymentMethod extends BaseEntity {
    private Long id;
    private String code;
    private String name;
    private PaymentMethodType type;
    private boolean active;

    public static PaymentMethod create(String code, String name, PaymentMethodType type, boolean active) {
        if (code == null || code.isBlank()) {
            throw new BadRequestException("Payment method code required");
        }
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Payment method name required");
        }
        if (type == null) {
            throw new BadRequestException("Payment method type required");
        }

        PaymentMethod pm = new PaymentMethod();
        pm.code = code.trim().toUpperCase();
        pm.name = name.trim();
        pm.type = type;
        pm.active = active;
        return pm;
    }

    public static PaymentMethod reconstruct(
            Long id,
            String code,
            String name,
            PaymentMethodType type,
            boolean active,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        PaymentMethod pm = new PaymentMethod();
        pm.id = id;
        pm.code = code;
        pm.name = name;
        pm.type = type;
        pm.active = active;
        pm.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
        return pm;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new BadRequestException("Payment method name required");
        }
        this.name = newName.trim();
    }

    public void changeType(PaymentMethodType newType) {
        if (newType == null) {
            throw new BadRequestException("Payment method type required");
        }
        this.type = newType;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}

