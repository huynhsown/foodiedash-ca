package com.ute.foodiedash.infrastructure.persistence.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.jpa.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, Long> {
    List<MenuItemJpaEntity> findByMenuIdInAndDeletedAtIsNull(
            List<Long> menuIds
    );

    @Modifying
    @Query("""
                UPDATE MenuItemJpaEntity
                SET deletedAt = CURRENT_TIMESTAMP
                WHERE id = :id
            """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
                UPDATE MenuItemJpaEntity
                SET deletedAt = NULL
                WHERE id = :id
            """)
    void restoreById(@Param("id") Long id);

    @Query("""
                SELECT CASE WHEN COUNT(mi) > 0 THEN true ELSE false END
                FROM MenuItemJpaEntity mi
                WHERE mi.menuId = :menuId
                AND mi.deletedAt IS NULL
            """)
    boolean existsByMenuIdAndDeletedAtIsNull(@Param("menuId") Long menuId);

    @Query("""
                SELECT mi FROM MenuItemJpaEntity mi
                WHERE mi.menuId = :menuId
                AND (:includeDeleted = true OR mi.deletedAt IS NULL)
            """)
    List<MenuItemJpaEntity> findByMenuId(
            @Param("menuId") Long menuId,
            @Param("includeDeleted") boolean includeDeleted);

    @Query("""
                SELECT mi FROM MenuItemJpaEntity mi
                WHERE mi.restaurantId = :restaurantId
                AND (:includeDeleted = true OR mi.deletedAt IS NULL)
            """)
    List<MenuItemJpaEntity> findByRestaurantId(
            @Param("restaurantId") Long restaurantId,
            @Param("includeDeleted") boolean includeDeleted);
}
