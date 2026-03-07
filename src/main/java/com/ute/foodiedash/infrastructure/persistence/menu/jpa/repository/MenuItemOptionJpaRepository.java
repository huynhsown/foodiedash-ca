package com.ute.foodiedash.infrastructure.persistence.menu.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemOptionJpaRepository extends JpaRepository<MenuItemOptionJpaEntity, Long> {

    List<MenuItemOptionJpaEntity> findByMenuItemIdAndDeletedAtIsNull(Long menuItemId);

    @Modifying
    @Query("""
                UPDATE MenuItemOptionJpaEntity
                SET deletedAt = CURRENT_TIMESTAMP
                WHERE id = :id
            """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
                UPDATE MenuItemOptionJpaEntity
                SET deletedAt = NULL
                WHERE id = :id
            """)
    void restoreById(@Param("id") Long id);
}
