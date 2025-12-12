CREATE TABLE order_payments
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT                              NOT NULL,
    payment_method VARCHAR(30)                         NOT NULL,
    payment_status VARCHAR(30)                         NOT NULL,
    transaction_id VARCHAR(255) NULL,
    paid_at        TIMESTAMP NULL,
    refunded_at        TIMESTAMP NULL,

    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by     VARCHAR(255) NULL,
    updated_by     VARCHAR(255) NULL,
    deleted_at     TIMESTAMP NULL,
    version        BIGINT    DEFAULT 0                 NOT NULL,

    CONSTRAINT uq_order_payment UNIQUE (order_id),

    CONSTRAINT fk_payment_order
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON DELETE CASCADE
);