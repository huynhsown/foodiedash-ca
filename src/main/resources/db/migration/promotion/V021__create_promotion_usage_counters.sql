CREATE TABLE promotion_usage_counters
(
    promotion_id BIGINT PRIMARY KEY,
    total_used   INT                DEFAULT 0,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255),
    deleted_at   TIMESTAMP NULL,

    version      BIGINT    NOT NULL DEFAULT 0,

    CONSTRAINT fk_counter_promo
        FOREIGN KEY (promotion_id) REFERENCES promotions (id)
);
