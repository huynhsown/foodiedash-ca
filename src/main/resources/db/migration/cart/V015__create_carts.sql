CREATE TABLE IF NOT EXISTS carts
(
    id            BIGSERIAL   PRIMARY KEY,
    user_id       BIGINT      NOT NULL,
    restaurant_id BIGINT      NOT NULL,
    status        VARCHAR(20) NOT NULL,
    expires_at    TIMESTAMP   NOT NULL,

    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP   NULL,

    version       BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT uk_cart_user_restaurant_status UNIQUE (user_id, restaurant_id, status)
);
