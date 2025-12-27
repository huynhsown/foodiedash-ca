CREATE TABLE IF NOT EXISTS cart_items
(
    id              BIGSERIAL PRIMARY KEY,
    cart_id         BIGINT NOT NULL,
    menu_item_id    BIGINT NOT NULL,
    name            VARCHAR(255) NOT NULL,
    image_url       VARCHAR(500),
    quantity        INT NOT NULL DEFAULT 1,
    unit_price      DECIMAL(12, 2) NOT NULL,
    total_price     DECIMAL(12, 2) NOT NULL,
    notes           TEXT,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    deleted_at      TIMESTAMP NULL,

    version         BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE
);
