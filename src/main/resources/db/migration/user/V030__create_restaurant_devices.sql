CREATE TABLE restaurant_devices
(
    id            BIGSERIAL    PRIMARY KEY,

    user_id       BIGINT       NOT NULL,
    restaurant_id BIGINT       NOT NULL,
    device_name   VARCHAR(255) NOT NULL,
    last_login_at TIMESTAMP    NULL,

    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP    NULL,

    version       BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT uq_device_user UNIQUE (user_id),

    CONSTRAINT fk_device_user
        FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT fk_device_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);