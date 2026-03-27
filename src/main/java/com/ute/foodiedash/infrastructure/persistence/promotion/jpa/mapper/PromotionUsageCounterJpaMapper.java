package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.mapper;

import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionUsageCounterJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionUsageCounterJpaMapper {
    PromotionUsageCounter toDomain(PromotionUsageCounterJpaEntity jpaEntity);
    PromotionUsageCounterJpaEntity toJpaEntity(PromotionUsageCounter domain);
}
