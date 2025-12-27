package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.repository;

import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PromotionJpaRepository extends JpaRepository<PromotionJpaEntity, Long> {

    Optional<PromotionJpaEntity> findByCode(String code);

    Optional<PromotionJpaEntity> findByCodeAndDeletedAtIsNull(String code);

    boolean existsByCode(String code);

    @Query("""
            SELECT p
            FROM PromotionJpaEntity p
            WHERE (:code IS NULL OR p.code = :code)
              AND (:status IS NULL OR p.status = :status)
              AND (:type IS NULL OR p.type = :type)
              AND (:eligibilityRule IS NULL OR p.eligibilityRule = :eligibilityRule)
              AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:startFrom IS NULL OR p.startAt >= :startFrom)
              AND (:startTo IS NULL OR p.startAt <= :startTo)
              AND (:endFrom IS NULL OR p.endAt >= :endFrom)
              AND (:endTo IS NULL OR p.endAt <= :endTo)
              AND (
                    :deleted IS NULL
                 OR (:deleted = TRUE  AND p.deletedAt IS NOT NULL)
                 OR (:deleted = FALSE AND p.deletedAt IS NULL)
              )
            """)
    Page<PromotionJpaEntity> search(
        @Param("code") String code,
        @Param("status") PromotionStatus status,
        @Param("type") PromotionType type,
        @Param("eligibilityRule") EligibilityRule eligibilityRule,
        @Param("name") String name,
        @Param("startFrom") LocalDateTime startFrom,
        @Param("startTo") LocalDateTime startTo,
        @Param("endFrom") LocalDateTime endFrom,
        @Param("endTo") LocalDateTime endTo,
        @Param("deleted") Boolean deleted,
        Pageable pageable
    );

    @Modifying
    @Query("""
            UPDATE PromotionJpaEntity
            SET deletedAt = CURRENT_TIMESTAMP
            WHERE id = :id
            """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
            UPDATE PromotionJpaEntity
            SET deletedAt = NULL
            WHERE id = :id
            """)
    void restoreById(@Param("id") Long id);
}
