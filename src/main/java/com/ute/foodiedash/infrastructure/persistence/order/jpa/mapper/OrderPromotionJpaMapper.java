package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderPromotion;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderPromotionJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderPromotionJpaMapper {

    default OrderPromotion toDomain(OrderPromotionJpaEntity e) {
        if (e == null) {
            return null;
        }
        return OrderPromotion.reconstruct(
            e.getId(),
            e.getOrder() != null ? e.getOrder().getId() : null,
            e.getPromotionId(),
            e.getPromotionCode(),
            e.getDiscountAmount(),
            e.getCreatedAt(),
            e.getUpdatedAt(),
            e.getCreatedBy(),
            e.getUpdatedBy(),
            e.getDeletedAt(),
            e.getVersion()
        );
    }

    @Mapping(target = "order", ignore = true)
    OrderPromotionJpaEntity toJpaEntity(OrderPromotion domain);

    default void updateEntity(@MappingTarget OrderPromotionJpaEntity e, OrderPromotion domain) {
        e.setPromotionId(domain.getPromotionId());
        e.setPromotionCode(domain.getPromotionCode());
        e.setDiscountAmount(domain.getDiscountAmount());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }
}
