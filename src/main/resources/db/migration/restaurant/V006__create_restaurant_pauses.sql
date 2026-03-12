CREATE TABLE IF NOT EXISTS restaurant_pauses
(
    id            BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    reason        VARCHAR(255) NOT NULL,
    paused_from   TIMESTAMP NOT NULL,
    paused_to     TIMESTAMP NOT NULL,

    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_pause_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);
