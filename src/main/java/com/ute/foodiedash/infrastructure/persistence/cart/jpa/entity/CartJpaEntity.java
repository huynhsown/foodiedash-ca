package com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity;

import com.ute.foodiedash.domain.cart.enums.CartStatus;
import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class CartJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
