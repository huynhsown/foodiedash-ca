package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.restaurant.model.RestaurantImage;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantImageRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantImageJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.RestaurantImageJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.RestaurantImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestaurantImageRepositoryAdapter implements RestaurantImageRepository {
    private final RestaurantImageJpaRepository jpaRepository;
    private final RestaurantImageJpaMapper mapper;

    @Override
    public RestaurantImage save(RestaurantImage image) {
        RestaurantImageJpaEntity jpaEntity = mapper.toJpaEntity(image);
        RestaurantImageJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RestaurantImage> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<RestaurantImage> findFirstByRestaurantId(Long restaurantId) {
        return jpaRepository.findFirstByRestaurantId(restaurantId)
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
