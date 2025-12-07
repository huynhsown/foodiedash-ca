package com.ute.foodiedash.infrastructure.persistence.user.jpa.entity;

import com.ute.foodiedash.domain.user.enums.RoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "user_roles")
public class UserRoleJpaEntity {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;
}

