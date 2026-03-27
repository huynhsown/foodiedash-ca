CREATE TABLE merchant_restaurants
(
    id            BIGSERIAL PRIMARY KEY,

    user_id       BIGINT    NOT NULL,
    restaurant_id BIGINT    NOT NULL,

    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT    NOT NULL DEFAULT 0,

    CONSTRAINT uq_merchant_restaurant UNIQUE (user_id, restaurant_id),

    CONSTRAINT fk_mr_user
        FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT fk_mr_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);