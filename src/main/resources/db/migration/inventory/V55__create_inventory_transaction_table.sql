CREATE TABLE inventory_transactions
(
    id                BIGSERIAL PRIMARY KEY,
    inventory_item_id BIGINT         NOT NULL,
    transaction_type  VARCHAR(20)    NOT NULL,
    quantity_change   DECIMAL(12, 3) NOT NULL,
    quantity_before   DECIMAL(12, 3) NOT NULL,
    quantity_after    DECIMAL(12, 3) NOT NULL,
    reference_type    VARCHAR(30),
    reference_id      BIGINT,

    note              VARCHAR(255),

    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(255),
    updated_by       VARCHAR(255),
    deleted_at       TIMESTAMP,

    version          BIGINT         NOT NULL DEFAULT 0,

    CONSTRAINT fk_inventory_transaction_item
        FOREIGN KEY (inventory_item_id)
            REFERENCES inventory_items (id)
);