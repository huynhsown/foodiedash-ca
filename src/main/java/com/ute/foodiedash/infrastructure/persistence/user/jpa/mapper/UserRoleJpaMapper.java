package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.UserRole;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserRoleId;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserRoleJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleJpaMapper {

    @Mapping(target = "userId", source = "user.id")
    UserRole toDomain(UserRoleJpaEntity jpaEntity);

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
