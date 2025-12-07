package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantCategoryMapJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantCategoryMapJpaRepository extends JpaRepository<RestaurantCategoryMapJpaEntity, Long> {

    @Modifying
    @Query(
            """
                        UPDATE RestaurantCategoryMapJpaEntity
                        SET deletedAt = CURRENT_TIMESTAMP
                        WHERE id = :id
                    """
    )
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query(
            """
                        UPDATE RestaurantCategoryMapJpaEntity
                        SET deletedAt = NULL
                        WHERE id = :id
                    """
    )
    void restoreById(@Param("id") Long id);

    @Query("""
                SELECT rcm FROM RestaurantCategoryMapJpaEntity rcm
                WHERE rcm.restaurantId = :restaurantId
                  AND (:includeDeleted = true OR rcm.deletedAt IS NULL)
            """)
    List<RestaurantCategoryMapJpaEntity> findByRestaurantId(
            @Param("restaurantId") Long restaurantId,
            @Param("includeDeleted") boolean includeDeleted
    );
}
