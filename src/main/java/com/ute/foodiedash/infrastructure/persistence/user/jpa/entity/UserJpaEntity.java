package com.ute.foodiedash.infrastructure.persistence.user.jpa.entity;

import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 20, unique = true)
    private String phone;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerProfileJpaEntity customerProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private MerchantProfileJpaEntity merchantProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private DriverProfileJpaEntity driverProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerAddressJpaEntity> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleJpaEntity> roles = new ArrayList<>();


    public void addAddress(CustomerAddressJpaEntity address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(CustomerAddressJpaEntity address) {
        addresses.remove(address);
        address.setUser(null);
    }

    public void addRole(UserRoleJpaEntity role) {
        roles.add(role);
        role.setUser(this);
    }

    public void removeRole(UserRoleJpaEntity role) {
        roles.remove(role);
        role.setUser(null);
    }
}
