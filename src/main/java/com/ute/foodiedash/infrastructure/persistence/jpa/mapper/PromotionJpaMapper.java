package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.PromotionJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionJpaMapper {
    Promotion toDomain(PromotionJpaEntity jpaEntity);
    PromotionJpaEntity toJpaEntity(Promotion domain);
}
