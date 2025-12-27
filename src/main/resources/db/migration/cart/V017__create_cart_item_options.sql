CREATE TABLE IF NOT EXISTS cart_item_options
(
    id                  BIGSERIAL PRIMARY KEY,
    cart_item_id        BIGINT NOT NULL,
    option_id           BIGINT NOT NULL,
    option_name         VARCHAR(255) NOT NULL,
    required            BOOLEAN NOT NULL,
    min_value           INT,
    max_value           INT,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP NULL,

    version             BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_cart_item_option_cart_item FOREIGN KEY (cart_item_id) REFERENCES cart_items (id) ON DELETE CASCADE
);
