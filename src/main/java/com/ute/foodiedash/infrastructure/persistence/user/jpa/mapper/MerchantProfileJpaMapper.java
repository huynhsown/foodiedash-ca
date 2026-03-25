package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.MerchantProfile;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.MerchantProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MerchantProfileJpaMapper {
    default MerchantProfile toDomain(MerchantProfileJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        Long userId = jpaEntity.getUser() != null ? jpaEntity.getUser().getId() : null;
        return MerchantProfile.reconstruct(
                jpaEntity.getId(),
                userId,
                jpaEntity.getBusinessName(),
                jpaEntity.getBusinessLicense(),
                jpaEntity.getTaxCode(),
                jpaEntity.getBankName(),
                jpaEntity.getBankAccount(),
                jpaEntity.getBankHolderName(),
                jpaEntity.getContactEmail(),
                jpaEntity.getContactPhone(),
                jpaEntity.getMerchantVerificationStatus(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }

    MerchantProfileJpaEntity toJpaEntity(MerchantProfile merchant);

    default MerchantProfileJpaEntity toJpaEntity(MerchantProfile merchant, UserJpaEntity user) {
        MerchantProfileJpaEntity jpaEntity = toJpaEntity(merchant);
        jpaEntity.setUser(user);
        return jpaEntity;
    }

    default void updateEntity(@MappingTarget MerchantProfileJpaEntity e, MerchantProfile domain) {
        e.setBusinessName(domain.getBusinessName());
        e.setBusinessLicense(domain.getBusinessLicense());
        e.setTaxCode(domain.getTaxCode());
        e.setBankName(domain.getBankName());
        e.setBankAccount(domain.getBankAccount());
        e.setBankHolderName(domain.getBankHolderName());
        e.setContactEmail(domain.getContactEmail());
        e.setContactPhone(domain.getContactPhone());
        e.setMerchantVerificationStatus(domain.getMerchantVerificationStatus());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }
}
