package com.ute.foodiedash.infrastructure.persistence.restaurant.adapter;

import com.ute.foodiedash.domain.restaurant.model.RestaurantPreparationSetting;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPreparationSettingRepository;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantPreparationSettingJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper.RestaurantPreparationSettingJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.repository.RestaurantPreparationSettingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantPreparationSettingRepositoryAdapter implements RestaurantPreparationSettingRepository {
    private final RestaurantPreparationSettingJpaRepository jpaRepository;
    private final RestaurantPreparationSettingJpaMapper mapper;

    @Override
    public RestaurantPreparationSetting save(RestaurantPreparationSetting setting) {
        RestaurantPreparationSettingJpaEntity jpaEntity = mapper.toJpaEntity(setting);
        RestaurantPreparationSettingJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RestaurantPreparationSetting> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<RestaurantPreparationSetting> findByRestaurantId(Long restaurantId, boolean includeDeleted) {
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
