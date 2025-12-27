CREATE TABLE IF NOT EXISTS menu_item_option_values
(
    id          BIGSERIAL      PRIMARY KEY,
    option_id   BIGINT         NOT NULL,
    name        VARCHAR(255)   NOT NULL,
    extra_price DECIMAL(12, 2) NOT NULL,

    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP NULL,

    version     BIGINT         NOT NULL DEFAULT 0,
    CONSTRAINT fk_option_value_option FOREIGN KEY (option_id) REFERENCES menu_item_options (id)
);
