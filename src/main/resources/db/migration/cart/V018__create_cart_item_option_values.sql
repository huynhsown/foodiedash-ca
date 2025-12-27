CREATE TABLE IF NOT EXISTS cart_item_option_values
(
    id                      BIGSERIAL PRIMARY KEY,
    cart_item_option_id     BIGINT NOT NULL,
    option_value_id         BIGINT NOT NULL,
    option_value_name       VARCHAR(255) NOT NULL,
    quantity                INT NOT NULL DEFAULT 1,
    extra_price             DECIMAL(12, 2) NOT NULL DEFAULT 0.00,

    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              VARCHAR(255),
    updated_by              VARCHAR(255),
    deleted_at              TIMESTAMP NULL,

    version                 BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_option_value_cart_item_option FOREIGN KEY (cart_item_option_id)
        REFERENCES cart_item_options (id) ON DELETE CASCADE
);
