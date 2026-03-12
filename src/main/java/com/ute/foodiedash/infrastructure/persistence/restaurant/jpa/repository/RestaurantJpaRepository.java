package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, Long> {

    Optional<RestaurantJpaEntity> findByIdAndDeletedAtIsNull(Long id);
    List<RestaurantJpaEntity> findAllByDeletedAtIsNull();

    @Modifying
    @Query(
        """
            UPDATE RestaurantJpaEntity
            SET deletedAt = CURRENT_TIMESTAMP
            WHERE id = :id
        """
    )
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query(
        """
            UPDATE RestaurantJpaEntity
            SET deletedAt = NULL
            WHERE id = :id
        """
    )
    void restoreById(@Param("id") Long id);

    boolean existsBySlug(String slug);

    RestaurantJpaEntity findBySlug(String slug);
}
