CREATE TABLE IF NOT EXISTS menus
(
    id            BIGSERIAL    PRIMARY KEY,
    restaurant_id BIGINT       NOT NULL,
    name          VARCHAR(255) NOT NULL,
    start_time    TIME         NOT NULL,
    end_time      TIME         NOT NULL,
    status VARCHAR(20)         NOT NULL,

    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT fk_menu_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);
