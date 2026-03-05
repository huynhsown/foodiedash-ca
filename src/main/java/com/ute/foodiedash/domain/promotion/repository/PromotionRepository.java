package com.ute.foodiedash.domain.promotion.repository;

import com.ute.foodiedash.domain.promotion.model.Promotion;

import java.util.Optional;

public interface PromotionRepository {
    Promotion save(Promotion promotion);
    Optional<Promotion> findById(Long id);
    Optional<Promotion> findByCode(String code);
    Optional<Promotion> findByCodeAndNotDeleted(String code);
    boolean existsByCode(String code);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
