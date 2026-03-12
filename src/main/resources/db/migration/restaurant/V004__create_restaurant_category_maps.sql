CREATE TABLE IF NOT EXISTS restaurant_category_maps
(
    id            BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT    NOT NULL,
    category_id   BIGINT    NOT NULL,

    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT    NOT NULL DEFAULT 0,
    CONSTRAINT fk_category_map_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
    CONSTRAINT fk_category_map_category FOREIGN KEY (category_id) REFERENCES restaurant_categories (id)
);
