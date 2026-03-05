package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.restaurant.model.RestaurantCategoryMap;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryMapRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantCategoryMapJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.RestaurantCategoryMapJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.RestaurantCategoryMapJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantCategoryMapRepositoryAdapter implements RestaurantCategoryMapRepository {
    private final RestaurantCategoryMapJpaRepository jpaRepository;
    private final RestaurantCategoryMapJpaMapper mapper;

    @Override
    public RestaurantCategoryMap save(RestaurantCategoryMap categoryMap) {
        RestaurantCategoryMapJpaEntity jpaEntity = mapper.toJpaEntity(categoryMap);
        RestaurantCategoryMapJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RestaurantCategoryMap> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<RestaurantCategoryMap> findByRestaurantId(Long restaurantId, boolean includeDeleted) {
        return jpaRepository.findByRestaurantId(restaurantId, includeDeleted).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void softDeleteById(Long id) {
        jpaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        jpaRepository.restoreById(id);
    }
}
