package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.restaurant.model.RestaurantRating;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRatingRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantRatingJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.RestaurantRatingJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.RestaurantRatingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantRatingRepositoryAdapter implements RestaurantRatingRepository {
    private final RestaurantRatingJpaRepository jpaRepository;
    private final RestaurantRatingJpaMapper mapper;

    @Override
    public RestaurantRating save(RestaurantRating restaurantRating) {
        RestaurantRatingJpaEntity jpaEntity = mapper.toJpaEntity(restaurantRating);
        RestaurantRatingJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RestaurantRating> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<RestaurantRating> findByRestaurantId(Long restaurantId, boolean includeDeleted) {
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
