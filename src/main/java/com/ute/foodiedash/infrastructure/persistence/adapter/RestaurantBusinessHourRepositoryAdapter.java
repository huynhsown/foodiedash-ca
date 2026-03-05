package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantBusinessHourRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantBusinessHourJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.RestaurantBusinessHourJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.RestaurantBusinessHourJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantBusinessHourRepositoryAdapter implements RestaurantBusinessHourRepository {
    private final RestaurantBusinessHourJpaRepository jpaRepository;
    private final RestaurantBusinessHourJpaMapper mapper;

    @Override
    public RestaurantBusinessHour save(RestaurantBusinessHour businessHour) {
        RestaurantBusinessHourJpaEntity jpaEntity = mapper.toJpaEntity(businessHour);
        RestaurantBusinessHourJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RestaurantBusinessHour> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<RestaurantBusinessHour> findByRestaurantId(Long restaurantId, boolean includeDeleted) {
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
