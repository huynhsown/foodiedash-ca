package com.ute.foodiedash.infrastructure.persistence.reviews.adapter;

import com.ute.foodiedash.domain.reviews.enums.ReviewStatus;
import com.ute.foodiedash.domain.reviews.model.Review;
import com.ute.foodiedash.domain.reviews.repository.ReviewRepository;
import com.ute.foodiedash.infrastructure.persistence.reviews.jpa.entity.ReviewJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.reviews.jpa.mapper.ReviewJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.reviews.jpa.repository.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;
    private final ReviewJpaMapper mapper;

    @Override
    public Review save(Review review) {
        ReviewJpaEntity jpaEntity;
        if (review.getId() == null) {
            jpaEntity = mapper.toJpaEntity(review);
        } else {
            jpaEntity = jpaRepository.findById(review.getId())
                    .orElseThrow();
            mapper.updateEntity(jpaEntity, review);
        }
        ReviewJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Review> findActiveById(Long id) {
        return jpaRepository
                .findByIdAndDeletedAtIsNullAndStatus(id, ReviewStatus.ACTIVE)
                .map(mapper::toDomain);
    }

    @Override
    public List<Review> findByCustomerId(Long customerId, int limit, int offset) {
        if (limit <= 0) {
            return List.of();
        }
        if (offset < 0) {
            offset = 0;
        }
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        return jpaRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId, pageable)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Review> findByOrderId(Long orderId, int limit, int offset) {
        if (limit <= 0) {
            return List.of();
        }
        if (offset < 0) {
            offset = 0;
        }
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        return jpaRepository
                .findByOrderIdOrderByCreatedAtDesc(orderId, pageable)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Review> findByRestaurantId(Long restaurantId, int limit, int offset) {
        if (limit <= 0) {
            return List.of();
        }
        if (offset < 0) {
            offset = 0;
        }
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        return jpaRepository
                .findByRestaurantIdAndDeletedAtIsNullAndStatusOrderByCreatedAtDesc(
                        restaurantId, ReviewStatus.ACTIVE, pageable)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public int countByCustomerId(Long customerId) {
        return jpaRepository.countByCustomerId(customerId);
    }

    @Override
    public int countByRestaurantId(Long restaurantId) {
        return jpaRepository.countByRestaurantIdAndDeletedAtIsNullAndStatus(
                restaurantId, ReviewStatus.ACTIVE);
    }

    @Override
    public long countByOrderId(Long orderId) {
        return jpaRepository.countByOrderId(orderId);
    }

    @Override
    public boolean existsByOrderIdAndCustomerId(Long orderId, Long customerId) {
        return jpaRepository.existsByOrderIdAndCustomerId(orderId, customerId);
    }

    @Override
    public Optional<Review> findByOrderIdAndCustomerId(Long orderId, Long customerId) {
        return jpaRepository.findByOrderIdAndCustomerId(orderId, customerId)
                .map(mapper::toDomain);
    }
}
