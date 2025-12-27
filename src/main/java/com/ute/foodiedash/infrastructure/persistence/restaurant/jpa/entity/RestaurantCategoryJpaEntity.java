package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity;

import com.ute.foodiedash.infrastructure.persistence.common.jpa.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant_categories")
public class RestaurantCategoryJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(name = "icon_url", columnDefinition = "text")
    private String iconUrl;

    @Column(columnDefinition = "text")
    private String description;
}
