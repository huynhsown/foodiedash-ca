package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.MerchantProfile;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.MerchantProfileJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import org.mapstruct.Mapper;

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
                jpaEntity.getVerificationStatus(),
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
}
