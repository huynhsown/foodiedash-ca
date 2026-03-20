package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.CustomerProfile;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerProfileJpaMapper {

    default CustomerProfile toDomain(CustomerProfileJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        Long userId = jpaEntity.getUser() != null ? jpaEntity.getUser().getId() : null;
        return CustomerProfile.reconstruct(
                jpaEntity.getId(),
                userId,
                jpaEntity.getDateOfBirth(),
                jpaEntity.getGender(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }

    CustomerProfileJpaEntity toJpaEntity(CustomerProfile domain);

    default CustomerProfileJpaEntity toJpaEntity(CustomerProfile domain, UserJpaEntity user) {
        CustomerProfileJpaEntity jpaEntity = toJpaEntity(domain);
        jpaEntity.setUser(user);
        return jpaEntity;
    }

    default void updateEntity(@MappingTarget CustomerProfileJpaEntity e, CustomerProfile domain) {
        e.setDateOfBirth(domain.getDateOfBirth());
        e.setGender(domain.getGender());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }
}
