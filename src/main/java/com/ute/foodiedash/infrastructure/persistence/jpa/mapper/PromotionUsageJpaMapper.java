package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.promotion.model.PromotionUsage;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.PromotionUsageJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionUsageJpaMapper {
    PromotionUsage toDomain(PromotionUsageJpaEntity jpaEntity);
    PromotionUsageJpaEntity toJpaEntity(PromotionUsage domain);
}
