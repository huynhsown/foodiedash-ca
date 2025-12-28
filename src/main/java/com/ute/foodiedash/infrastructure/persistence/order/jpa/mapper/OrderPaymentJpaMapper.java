package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderPayment;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderPaymentJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderPaymentJpaMapper {

    default OrderPayment toDomain(OrderPaymentJpaEntity e) {
        if (e == null) {
            return null;
        }
        return OrderPayment.reconstruct(
            e.getId(),
            e.getOrderId(),
            e.getPaymentMethod(),
            e.getPaymentStatus(),
            e.getTransactionId(),
            e.getPaidAt(),
            e.getRefundedAt(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getCreatedBy(),
            e.getUpdatedBy(),
            e.getDeletedAt(),
            e.getVersion()
        );
    }

    OrderPaymentJpaEntity toJpaEntity(OrderPayment domain);
}
