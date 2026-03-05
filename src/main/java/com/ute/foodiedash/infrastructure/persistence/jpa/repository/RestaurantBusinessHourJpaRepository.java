package com.ute.foodiedash.infrastructure.persistence.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantBusinessHourJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantBusinessHourJpaRepository extends JpaRepository<RestaurantBusinessHourJpaEntity, Long> {

    @Modifying
    @Query(
        """
            UPDATE RestaurantBusinessHourJpaEntity
            SET deletedAt = CURRENT_TIMESTAMP
            WHERE id = :id
        """
    )
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query(
        """
            UPDATE RestaurantBusinessHourJpaEntity
            SET deletedAt = NULL
            WHERE id = :id
        """
    )
    void restoreById(@Param("id") Long id);

    @Query("""
        SELECT rbh FROM RestaurantBusinessHourJpaEntity rbh
        WHERE rbh.restaurantId = :restaurantId
        AND (:includeDeleted = true OR rbh.deletedAt IS NULL)
        """)
    List<RestaurantBusinessHourJpaEntity> findByRestaurantId(
        @Param("restaurantId") Long restaurantId,
        @Param("includeDeleted") boolean includeDeleted
    );
}
