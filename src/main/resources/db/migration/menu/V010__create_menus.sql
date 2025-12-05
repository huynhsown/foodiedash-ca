CREATE TABLE IF NOT EXISTS menus
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT       NOT NULL,
    name          VARCHAR(255) NOT NULL,
    start_time    TIME         NOT NULL,
    end_time      TIME         NOT NULL,
    status VARCHAR(20)         NOT NULL,

    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT fk_menu_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
) ENGINE=InnoDB;
