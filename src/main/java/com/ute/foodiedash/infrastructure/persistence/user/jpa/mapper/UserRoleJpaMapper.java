package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.UserRole;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserRoleId;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserRoleJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleJpaMapper {

    default UserRole toDomain(UserRoleJpaEntity jpaEntity) {
        if (jpaEntity == null || jpaEntity.getId() == null) {
            return null;
        }
        Long userId = jpaEntity.getUser() != null ? jpaEntity.getUser().getId() : jpaEntity.getId().getUserId();
        return UserRole.reconstruct(userId, jpaEntity.getId().getRoleName());
    }

    default UserRoleJpaEntity toJpaEntity(UserRole domain) {
        UserRoleJpaEntity entity = new UserRoleJpaEntity();
        UserRoleId id = new UserRoleId();
        id.setRoleName(domain.getRoleName());
        entity.setId(id);
        return entity;
    }

    default UserRoleJpaEntity toJpaEntity(UserRole domain, UserJpaEntity user) {
        UserRoleJpaEntity entity = toJpaEntity(domain);
        UserRoleId id = new UserRoleId();
        id.setUserId(user.getId());
        id.setRoleName(domain.getRoleName());
        entity.setId(id);
        entity.setUser(user);
        return entity;
    }
}
