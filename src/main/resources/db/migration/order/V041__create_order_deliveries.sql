CREATE TABLE order_deliveries
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id       BIGINT                              NOT NULL,
    driver_id      BIGINT NULL,
    address        VARCHAR(500)                        NOT NULL,
    lat            DECIMAL(10, 6)                      NOT NULL,
    lng            DECIMAL(10, 6)                      NOT NULL,
    receiver_name  VARCHAR(255)                        NOT NULL,
    receiver_phone VARCHAR(20)                         NOT NULL,
    note           VARCHAR(500) NULL,
    distance_km    DECIMAL(8, 2) NULL,
    eta_minutes    INT NULL,
    geometry       TEXT,
    picked_up_at   TIMESTAMP NULL,
    delivered_at   TIMESTAMP NULL,

    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by     VARCHAR(255) NULL,
    updated_by     VARCHAR(255) NULL,
    deleted_at     TIMESTAMP NULL,
    version        BIGINT    DEFAULT 0                 NOT NULL,

    CONSTRAINT uq_order_delivery UNIQUE (order_id),

    CONSTRAINT fk_delivery_order
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_delivery_driver
        FOREIGN KEY (driver_id) REFERENCES driver_profiles (id)
) ENGINE = INNODB;