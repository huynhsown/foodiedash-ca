CREATE TABLE order_status_histories
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT                              NOT NULL,
    status     VARCHAR(30)                         NOT NULL,
    note       VARCHAR(255) NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    deleted_at TIMESTAMP NULL,
    version    BIGINT    DEFAULT 0                 NOT NULL,

    CONSTRAINT fk_order_history
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON DELETE CASCADE
) ENGINE = INNODB;