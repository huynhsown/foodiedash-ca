package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.CustomerAddress;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerAddressJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerAddressJpaMapper {
    default CustomerAddress toDomain(CustomerAddressJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        Long userId = jpaEntity.getUser() != null ? jpaEntity.getUser().getId() : null;
        boolean defaultAddress = Boolean.TRUE.equals(jpaEntity.getDefaultAddress());
        return CustomerAddress.reconstruct(
                jpaEntity.getId(),
                userId,
                jpaEntity.getLabel(),
                jpaEntity.getAddress(),
                jpaEntity.getLat(),
                jpaEntity.getLng(),
                jpaEntity.getReceiverName(),
                jpaEntity.getReceiverPhone(),
                jpaEntity.getNote(),
                defaultAddress,
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }

    CustomerAddressJpaEntity toJpaEntity(CustomerAddress domain);

    default CustomerAddressJpaEntity toJpaEntity(CustomerAddress domain, UserJpaEntity user) {
        CustomerAddressJpaEntity jpaEntity = toJpaEntity(domain);
        jpaEntity.setUser(user);
        return jpaEntity;
    }

    default void updateEntity(@MappingTarget CustomerAddressJpaEntity e, CustomerAddress domain) {
        e.setLabel(domain.getLabel());
        e.setAddress(domain.getAddress());
        e.setLat(domain.getLat());
        e.setLng(domain.getLng());
        e.setReceiverName(domain.getReceiverName());
        e.setReceiverPhone(domain.getReceiverPhone());
        e.setNote(domain.getNote());
        e.setDefaultAddress(domain.isDefaultAddress());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }
}
