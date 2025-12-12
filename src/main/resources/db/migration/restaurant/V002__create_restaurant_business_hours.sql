CREATE TABLE IF NOT EXISTS restaurant_business_hours
(
    id            BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT    NOT NULL,
    day_of_week   INT       NOT NULL,
    open_time     TIME      NOT NULL,
    close_time    TIME      NOT NULL,

    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT    NOT NULL DEFAULT 0,
    CONSTRAINT fk_business_hour_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);
