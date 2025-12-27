package com.ute.foodiedash.infrastructure.persistence.paymentmethod.jpa.mapper;

import com.ute.foodiedash.domain.paymentmethod.model.PaymentMethod;
import com.ute.foodiedash.infrastructure.persistence.paymentmethod.jpa.entity.PaymentMethodJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodJpaMapper {
    default PaymentMethod toDomain(PaymentMethodJpaEntity e) {
        if (e == null) {
            return null;
        }
        return PaymentMethod.reconstruct(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getType(),
                e.isActive(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    PaymentMethodJpaEntity toJpaEntity(PaymentMethod domain);
}

