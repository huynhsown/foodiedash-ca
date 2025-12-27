CREATE TABLE order_item_option_values
(
    id                   BIGSERIAL PRIMARY KEY,
    order_item_option_id BIGINT                                   NOT NULL,
    option_value_id      BIGINT                                   NOT NULL,
    option_value_name    VARCHAR(255)                             NOT NULL,
    quantity             INT            DEFAULT 1                 NOT NULL,
    extra_price          DECIMAL(12, 2) DEFAULT 0                 NOT NULL,

    created_at           TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at           TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by           VARCHAR(255) NULL,
    updated_by           VARCHAR(255) NULL,
    deleted_at           TIMESTAMP NULL,
    version              BIGINT         DEFAULT 0                 NOT NULL,

    CONSTRAINT fk_order_option_value
        FOREIGN KEY (order_item_option_id)
            REFERENCES order_item_options (id)
            ON DELETE CASCADE
);