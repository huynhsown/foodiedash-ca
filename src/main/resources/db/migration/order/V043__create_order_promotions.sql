CREATE TABLE order_promotions
(
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT                              NOT NULL,
    promotion_id    BIGINT                              NOT NULL,
    promotion_code  VARCHAR(50)                         NOT NULL,
    discount_amount DECIMAL(12, 2)                      NOT NULL,

    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by     VARCHAR(255) NULL,
    updated_by     VARCHAR(255) NULL,
    deleted_at     TIMESTAMP NULL,
    version        BIGINT    DEFAULT 0                 NOT NULL,

    CONSTRAINT uq_order_promo UNIQUE (order_id, promotion_id),

    CONSTRAINT fk_order_promo_order
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON DELETE CASCADE
);