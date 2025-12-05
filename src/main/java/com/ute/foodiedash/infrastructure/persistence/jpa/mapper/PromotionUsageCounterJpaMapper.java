package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.PromotionUsageCounterJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionUsageCounterJpaMapper {
    PromotionUsageCounter toDomain(PromotionUsageCounterJpaEntity jpaEntity);
    PromotionUsageCounterJpaEntity toJpaEntity(PromotionUsageCounter domain);
}
