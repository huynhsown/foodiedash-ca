CREATE TABLE promotion_restaurants
(
    id            BIGSERIAL PRIMARY KEY,

    promotion_id  BIGINT    NOT NULL,
    restaurant_id BIGINT    NOT NULL,

    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT    NOT NULL DEFAULT 0,

    CONSTRAINT fk_promo_restaurant_promo
        FOREIGN KEY (promotion_id) REFERENCES promotions (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_promo_restaurant UNIQUE (promotion_id, restaurant_id)
);
