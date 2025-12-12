CREATE TABLE IF NOT EXISTS restaurant_preparation_settings
(
    id                  BIGSERIAL PRIMARY KEY,
    restaurant_id       BIGINT    NOT NULL,
    prep_time_min       INT       NOT NULL,
    prep_time_max       INT       NOT NULL,
    slot_duration       INT       NOT NULL,
    max_orders_per_slot INT       NOT NULL,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP NULL,

    version             BIGINT    NOT NULL DEFAULT 0,
    CONSTRAINT fk_prep_setting_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);
