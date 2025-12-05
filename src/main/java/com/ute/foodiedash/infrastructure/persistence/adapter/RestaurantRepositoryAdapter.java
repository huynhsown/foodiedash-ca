package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.RestaurantJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.RestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryAdapter implements RestaurantRepository {
    private final RestaurantJpaRepository jpaRepository;
    private final RestaurantJpaMapper mapper;

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantJpaEntity jpaEntity = mapper.toJpaEntity(restaurant);
        RestaurantJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Restaurant> findByIdAndDeletedAtIsNull(Long id) {
        return jpaRepository.findByIdAndDeletedAtIsNull(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Restaurant> findBySlug(String slug) {
        RestaurantJpaEntity jpaEntity = jpaRepository.findBySlug(slug);
        if (jpaEntity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(jpaEntity));
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
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
