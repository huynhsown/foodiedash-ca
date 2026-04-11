CREATE TABLE inventory_items
(
    id               BIGSERIAL PRIMARY KEY,
    restaurant_id    BIGINT         NOT NULL,
    sku              VARCHAR(50)    NOT NULL,
    name             VARCHAR(255)   NOT NULL,
    unit             VARCHAR(20)    NOT NULL,
    quantity_on_hand DECIMAL(12, 3) NOT NULL DEFAULT 0,
    reorder_level    DECIMAL(12, 3) NOT NULL DEFAULT 0,
    reorder_quantity DECIMAL(12, 3) NOT NULL DEFAULT 0,
    unit_cost        DECIMAL(12, 2) NOT NULL,
    status           VARCHAR(20)    NOT NULL,

    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(255),
    updated_by       VARCHAR(255),
    deleted_at       TIMESTAMP,

    version          BIGINT         NOT NULL DEFAULT 0,

    CONSTRAINT uq_inventory_item_sku UNIQUE (restaurant_id, sku)
);

CREATE INDEX idx_inventory_restaurant ON inventory_items(restaurant_id);