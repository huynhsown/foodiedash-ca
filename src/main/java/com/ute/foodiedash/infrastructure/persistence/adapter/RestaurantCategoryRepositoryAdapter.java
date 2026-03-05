package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.restaurant.model.RestaurantCategory;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantCategoryJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.RestaurantCategoryJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.RestaurantCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantCategoryRepositoryAdapter implements RestaurantCategoryRepository {
    private final RestaurantCategoryJpaRepository jpaRepository;
    private final RestaurantCategoryJpaMapper mapper;

    @Override
    public RestaurantCategory save(RestaurantCategory category) {
        RestaurantCategoryJpaEntity jpaEntity = mapper.toJpaEntity(category);
        RestaurantCategoryJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RestaurantCategory> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<RestaurantCategory> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantCategory> findByIdIn(List<Long> categoryIds) {
        return jpaRepository.findByIdIn(categoryIds).stream()
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
