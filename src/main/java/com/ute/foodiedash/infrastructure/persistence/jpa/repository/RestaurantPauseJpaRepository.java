package com.ute.foodiedash.infrastructure.persistence.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantPauseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantPauseJpaRepository extends JpaRepository<RestaurantPauseJpaEntity, Long> {

    @Modifying
    @Query(
        """
            UPDATE RestaurantPauseJpaEntity
            SET deletedAt = CURRENT_TIMESTAMP
            WHERE id = :id
        """
    )
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query(
        """
            UPDATE RestaurantPauseJpaEntity
            SET deletedAt = NULL
            WHERE id = :id
        """
    )
    void restoreById(@Param("id") Long id);
}
