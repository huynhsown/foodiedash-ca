package com.ute.foodiedash.infrastructure.persistence.user.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerAddressJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAddressJpaRepository extends JpaRepository<CustomerAddressJpaEntity, Long> {
    List<CustomerAddressJpaEntity> findByUserIdAndDeletedAtIsNull(Long userId);

    @Query("""
        SELECT a FROM CustomerAddressJpaEntity a
        WHERE a.user.id = :userId AND a.defaultAddress = true AND a.deletedAt IS NULL
    """)
    Optional<CustomerAddressJpaEntity> findDefaultByUserId(@Param("userId") Long userId);
}
