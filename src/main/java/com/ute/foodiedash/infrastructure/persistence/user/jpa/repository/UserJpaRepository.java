package com.ute.foodiedash.infrastructure.persistence.user.jpa.repository;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Query("""
        SELECT DISTINCT u FROM UserJpaEntity u
        LEFT JOIN FETCH u.roles
        WHERE u.id = :id AND u.deletedAt IS NULL
    """)
    Optional<UserJpaEntity> findByIdWithRoles(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT u FROM UserJpaEntity u
        LEFT JOIN FETCH u.addresses
        WHERE u.id = :id AND u.deletedAt IS NULL
    """)
    Optional<UserJpaEntity> findByIdWithAddresses(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT u FROM UserJpaEntity u
        LEFT JOIN FETCH u.customerProfile
        LEFT JOIN FETCH u.merchantProfile
        LEFT JOIN FETCH u.driverProfile
        WHERE u.id = :id AND u.deletedAt IS NULL
    """)
    Optional<UserJpaEntity> findByIdWithProfile(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT u FROM UserJpaEntity u
        LEFT JOIN FETCH u.roles
        LEFT JOIN FETCH u.addresses
        LEFT JOIN FETCH u.customerProfile
        LEFT JOIN FETCH u.merchantProfile
        LEFT JOIN FETCH u.driverProfile
        WHERE u.id = :id AND u.deletedAt IS NULL
    """)
    Optional<UserJpaEntity> findByIdWithAll(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT u FROM UserJpaEntity u
        LEFT JOIN FETCH u.roles
        WHERE u.email = :email AND u.deletedAt IS NULL
    """)
    Optional<UserJpaEntity> findByEmailWithRoles(@Param("email") String email);

    @Modifying
    @Query("""
        UPDATE UserJpaEntity u
        SET u.deletedAt = CURRENT_TIMESTAMP
        WHERE u.id = :id
    """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
        UPDATE UserJpaEntity u
        SET u.deletedAt = NULL
        WHERE u.id = :id
    """)
    void restoreById(@Param("id") Long id);

    @Query("""
        SELECT COUNT(u) FROM UserJpaEntity u
        WHERE u.status = :status AND u.deletedAt IS NULL
    """)
    long countByStatus(@Param("status") UserStatus status);

    @Query("""
        SELECT CASE WHEN COUNT(mr) > 0 THEN true ELSE false END
        FROM MerchantRestaurantJpaEntity mr
        WHERE mr.user.id = :userId
          AND mr.restaurantId = :restaurantId
          AND mr.deletedAt IS NULL
    """)
    boolean existsMerchantRestaurant(
            @Param("userId") Long userId,
            @Param("restaurantId") Long restaurantId
    );

    @Query("""
        SELECT u.id, u.fullName, u.avatarUrl
        FROM UserJpaEntity u
        WHERE u.id IN :ids AND u.deletedAt IS NULL
    """)
    List<Object[]> findBasicInfoByIdIn(@Param("ids") List<Long> ids);


    @Query("""
        SELECT DISTINCT u FROM UserJpaEntity u
        JOIN u.driverProfile dp
        WHERE
            (:keyword IS NULL OR
                LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR u.phone LIKE CONCAT('%', :keyword, '%')
                OR u.email LIKE CONCAT('%', :keyword, '%')
                OR dp.licenseNumber LIKE CONCAT('%', :keyword, '%')
            )
        AND (:userStatus IS NULL
             OR u.status = :userStatus)
        AND (:driverVerificationStatus IS NULL
                         OR dp.driverVerificationStatus = :driverVerificationStatus)
        AND (:vehicleType IS NULL
                     OR dp.vehicleType = :vehicleType)
        AND (u.createdAt >= :createdFrom)
        AND (u.createdAt <= :createdTo)
    """)
    Page<UserJpaEntity> searchDrivers(
            @Param("keyword") String keyword,
            @Param("userStatus") UserStatus userStatus,
            @Param("driverVerificationStatus")
            DriverVerificationStatus driverVerificationStatus,
            @Param("vehicleType")
            VehicleType vehicleType,
            @Param("createdFrom")
            Instant createdFrom,
            @Param("createdTo")
            Instant createdTo,
            Pageable pageable
    );
}
