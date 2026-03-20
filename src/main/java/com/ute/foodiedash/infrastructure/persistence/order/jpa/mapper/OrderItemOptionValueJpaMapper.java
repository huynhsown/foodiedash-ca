package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderItemOptionValueJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemOptionValueJpaMapper {

    default OrderItemOptionValue toDomain(OrderItemOptionValueJpaEntity e) {
        if (e == null) {
            return null;
        }
        return OrderItemOptionValue.reconstruct(
            e.getId(),
            e.getOrderItemOption() != null ? e.getOrderItemOption().getId() : null,
            e.getOptionValueId(),
            e.getOptionValueName(),
            e.getQuantity(),
            e.getExtraPrice(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getCreatedBy(),
            e.getUpdatedBy(),
            e.getDeletedAt(),
            e.getVersion()
        );
    }

    @Mapping(target = "orderItemOption", ignore = true)
    OrderItemOptionValueJpaEntity toJpaEntity(OrderItemOptionValue domain);

    default void updateEntity(@MappingTarget OrderItemOptionValueJpaEntity e, OrderItemOptionValue domain) {
        e.setOptionValueId(domain.getOptionValueId());
        e.setOptionValueName(domain.getOptionValueName());
        e.setQuantity(domain.getQuantity());
        e.setExtraPrice(domain.getExtraPrice());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }
}
