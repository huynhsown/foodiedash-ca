package com.ute.foodiedash.infrastructure.persistence.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.jpa.entity.MenuJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuJpaRepository extends JpaRepository<MenuJpaEntity, Long> {

    List<MenuJpaEntity> findByRestaurantId(Long restaurantId);

    @Modifying
    @Query("""
                UPDATE MenuJpaEntity
                SET deletedAt = CURRENT_TIMESTAMP
                WHERE id = :id
            """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
                UPDATE MenuJpaEntity
                SET deletedAt = NULL
                WHERE id = :id
            """)
    void restoreById(@Param("id") Long id);
}
