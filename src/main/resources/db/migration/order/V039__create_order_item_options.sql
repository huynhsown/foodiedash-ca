CREATE TABLE order_item_options
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_item_id BIGINT                              NOT NULL,
    option_id     BIGINT                              NOT NULL,
    option_name   VARCHAR(255)                        NOT NULL,
    required      TINYINT(1)                          NOT NULL,
    min_value     INT NULL,
    max_value     INT NULL,

    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by    VARCHAR(255) NULL,
    updated_by    VARCHAR(255) NULL,
    deleted_at    TIMESTAMP NULL,
    version       BIGINT    DEFAULT 0                 NOT NULL,

    CONSTRAINT fk_order_item_option
        FOREIGN KEY (order_item_id)
            REFERENCES order_items (id)
            ON DELETE CASCADE
) ENGINE = INNODB;