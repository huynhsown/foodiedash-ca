package com.ute.foodiedash.infrastructure.persistence.order.jpa.mapper;

import com.ute.foodiedash.domain.order.model.OrderPromotion;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderPromotionJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
            e.getDiscountAmount()
        );
    }

    @Mapping(target = "order", ignore = true)
    OrderPromotionJpaEntity toJpaEntity(OrderPromotion domain);
}
