package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.restaurant.model.RestaurantPause;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPauseRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantPauseJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.RestaurantPauseJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.RestaurantPauseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestaurantPauseRepositoryAdapter implements RestaurantPauseRepository {
    private final RestaurantPauseJpaRepository jpaRepository;
    private final RestaurantPauseJpaMapper mapper;

    @Override
    public RestaurantPause save(RestaurantPause pause) {
        RestaurantPauseJpaEntity jpaEntity = mapper.toJpaEntity(pause);
        RestaurantPauseJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RestaurantPause> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
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
