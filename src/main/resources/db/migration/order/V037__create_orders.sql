CREATE TABLE orders
(
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(30)                              NOT NULL,
    customer_id     BIGINT                                   NOT NULL,
    restaurant_id   BIGINT                                   NOT NULL,
    status          VARCHAR(30)                              NOT NULL,
    subtotal_amount DECIMAL(12, 2)                           NOT NULL,
    discount_amount DECIMAL(12, 2) DEFAULT 0                 NOT NULL,
    delivery_fee    DECIMAL(12, 2) DEFAULT 0                 NOT NULL,
    total_amount    DECIMAL(12, 2)                           NOT NULL,
    placed_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    accepted_at     TIMESTAMP NULL,
    prepared_at     TIMESTAMP NULL,
    cancelled_at    TIMESTAMP NULL,
    cancel_reason   VARCHAR(255) NULL,

    created_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by      VARCHAR(255) NULL,
    updated_by      VARCHAR(255) NULL,
    deleted_at      TIMESTAMP NULL,
    version         BIGINT         DEFAULT 0                 NOT NULL,

    CONSTRAINT uq_order_code UNIQUE (code),

    CONSTRAINT fk_orders_user
        FOREIGN KEY (customer_id) REFERENCES customer_profiles (id),

    CONSTRAINT fk_orders_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);