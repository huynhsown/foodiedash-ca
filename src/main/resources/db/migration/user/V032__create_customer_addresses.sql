CREATE TABLE customer_addresses
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    user_id        BIGINT       NOT NULL,
    label          VARCHAR(50)  NOT NULL,
    address        VARCHAR(500) NOT NULL,
    lat            DECIMAL(10, 6) NOT NULL,
    lng            DECIMAL(10, 6) NOT NULL,
    receiver_name  VARCHAR(255),
    receiver_phone VARCHAR(20),
    note           VARCHAR(500),
    is_default     BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by     VARCHAR(255),
    updated_by     VARCHAR(255),
    deleted_at     TIMESTAMP    NULL,

    version        BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT fk_address_user
        FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = InnoDB;