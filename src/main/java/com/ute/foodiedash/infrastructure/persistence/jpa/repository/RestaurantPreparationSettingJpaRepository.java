package com.ute.foodiedash.infrastructure.persistence.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantPreparationSettingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantPreparationSettingJpaRepository extends JpaRepository<RestaurantPreparationSettingJpaEntity, Long> {

    @Modifying
    @Query(
        """
            UPDATE RestaurantPreparationSettingJpaEntity
            SET deletedAt = CURRENT_TIMESTAMP
            WHERE id = :id
        """
    )
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query(
        """
            UPDATE RestaurantPreparationSettingJpaEntity
            SET deletedAt = NULL
            WHERE id = :id
        """
    )
    void restoreById(@Param("id") Long id);

    @Query("""
        SELECT rps FROM RestaurantPreparationSettingJpaEntity rps
        WHERE rps.restaurantId = :restaurantId
        AND (:includeDeleted = true OR rps.deletedAt IS NULL)
        """)
    List<RestaurantPreparationSettingJpaEntity> findByRestaurantId(
        @Param("restaurantId") Long restaurantId,
        @Param("includeDeleted") boolean includeDeleted
    );
}
