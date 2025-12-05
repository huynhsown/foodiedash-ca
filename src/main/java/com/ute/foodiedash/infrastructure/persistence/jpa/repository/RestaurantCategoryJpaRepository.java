package com.ute.foodiedash.infrastructure.persistence.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantCategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantCategoryJpaRepository extends JpaRepository<RestaurantCategoryJpaEntity, Long> {
    @Modifying
    @Query(
        """
            UPDATE RestaurantCategoryJpaEntity
            SET deletedAt = CURRENT_TIMESTAMP
            WHERE id = :id
        """
    )
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query(
        """
            UPDATE RestaurantCategoryJpaEntity
            SET deletedAt = NULL
            WHERE id = :id
        """
    )
    void restoreById(@Param("id") Long id);

    List<RestaurantCategoryJpaEntity> findByIdIn(List<Long> categoryIds);
}
