package com.ute.foodiedash.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "promotion_usage_counters")
public class PromotionUsageCounterJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "total_used")
    private Integer totalUsed;
}
