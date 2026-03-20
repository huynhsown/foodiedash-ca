package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.mapper;

import com.ute.foodiedash.domain.promotion.model.PromotionUsage;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionUsageJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionUsageJpaMapper {
    PromotionUsage toDomain(PromotionUsageJpaEntity jpaEntity);
    PromotionUsageJpaEntity toJpaEntity(PromotionUsage domain);

    void updateEntity(@MappingTarget PromotionUsageJpaEntity entity, PromotionUsage domain);
}
