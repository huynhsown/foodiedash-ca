CREATE TABLE promotion_usages
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,

    promotion_id   BIGINT      NOT NULL,
    user_id    BIGINT      NOT NULL,
    order_id   BIGINT      NOT NULL,

    status     VARCHAR(20) NOT NULL,

    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP NULL,

    version    BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT fk_promo_usage_promo
        FOREIGN KEY (promotion_id) REFERENCES promotions (id),

    CONSTRAINT uq_promo_order UNIQUE (promotion_id, order_id)
);
