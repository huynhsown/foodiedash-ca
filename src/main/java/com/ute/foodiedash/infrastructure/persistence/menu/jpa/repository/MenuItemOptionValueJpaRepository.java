package com.ute.foodiedash.infrastructure.persistence.menu.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MenuItemOptionValueJpaRepository extends JpaRepository<MenuItemOptionValueJpaEntity, Long> {

    List<MenuItemOptionValueJpaEntity> findByOptionIdAndDeletedAt(Long optionId, Instant deletedAt);
    List<MenuItemOptionValueJpaEntity> findByOptionIdInAndDeletedAtIsNull(List<Long> optionId);

    @Modifying
    @Query(
        """
            UPDATE MenuItemOptionValueJpaEntity
            SET deletedAt = CURRENT_TIMESTAMP
            WHERE id = :id
        """
    )
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query(
        """
            UPDATE MenuItemOptionValueJpaEntity
            SET deletedAt = NULL
            WHERE id = :id
        """
    )
    void restoreById(@Param("id") Long id);
}
