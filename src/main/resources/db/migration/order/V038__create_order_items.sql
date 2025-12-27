CREATE TABLE order_items
(
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT                              NOT NULL,
    menu_item_id BIGINT                              NOT NULL,
    name         VARCHAR(255)                        NOT NULL,
    image_url    VARCHAR(500)                        NULL,
    quantity     INT                                 NOT NULL,
    unit_price   DECIMAL(12, 2)                      NOT NULL,
    total_price  DECIMAL(12, 2)                      NOT NULL,
    notes        TEXT                                NULL,

    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by   VARCHAR(255)                        NULL,
    updated_by   VARCHAR(255)                        NULL,
    deleted_at   TIMESTAMP                           NULL,
    version      BIGINT    DEFAULT 0                 NOT NULL,

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON DELETE CASCADE
);