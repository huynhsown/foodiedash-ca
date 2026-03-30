package com.ute.foodiedash.infrastructure.persistence.user.jpa.entity;

import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "restaurant_devices")
public class RestaurantDeviceJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(name = "device_name", nullable = false, length = 255)
    private String deviceName;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;
}

