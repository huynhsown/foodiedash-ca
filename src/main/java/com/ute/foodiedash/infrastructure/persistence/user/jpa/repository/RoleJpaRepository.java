package com.ute.foodiedash.infrastructure.persistence.user.jpa.repository;

import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {

    Optional<RoleJpaEntity> findByName(RoleName name);

    @Query("SELECT DISTINCT r FROM RoleJpaEntity r "
            + "LEFT JOIN FETCH r.permissionAssignments pa "
            + "LEFT JOIN FETCH pa.permission "
            + "WHERE r.id = :id")
    Optional<RoleJpaEntity> findByIdWithAssignments(@Param("id") Long id);

    @Query("SELECT DISTINCT r FROM RoleJpaEntity r "
            + "LEFT JOIN FETCH r.permissionAssignments pa "
            + "LEFT JOIN FETCH pa.permission "
            + "WHERE r.name = :name")
    Optional<RoleJpaEntity> findByNameWithAssignments(@Param("name") RoleName name);

    @Query("SELECT DISTINCT r FROM RoleJpaEntity r "
            + "LEFT JOIN FETCH r.permissionAssignments pa "
            + "LEFT JOIN FETCH pa.permission")
    List<RoleJpaEntity> findAllWithAssignments();
}
