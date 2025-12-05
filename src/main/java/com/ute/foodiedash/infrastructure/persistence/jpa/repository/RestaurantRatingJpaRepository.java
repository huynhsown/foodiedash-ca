package com.ute.foodiedash.infrastructure.persistence.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantRatingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRatingJpaRepository extends JpaRepository<RestaurantRatingJpaEntity, Long> {

    @Modifying
    @Query(
        """
            UPDATE RestaurantRatingJpaEntity
            SET deletedAt = CURRENT_TIMESTAMP
            WHERE id = :id
        """
    )
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query(
        """
            UPDATE RestaurantRatingJpaEntity
            SET deletedAt = NULL
            WHERE id = :id
        """
    )
    void restoreById(@Param("id") Long id);

    @Query("""
        SELECT rr FROM RestaurantRatingJpaEntity rr
        WHERE rr.restaurantId = :restaurantId
        AND (:includeDeleted = true OR rr.deletedAt IS NULL)
        """)
    List<RestaurantRatingJpaEntity> findByRestaurantId(
        @Param("restaurantId") Long restaurantId,
        @Param("includeDeleted") boolean includeDeleted
    );
}
