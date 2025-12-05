CREATE TABLE IF NOT EXISTS restaurant_images
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    restaurant_id BIGINT       NOT NULL,
    image_url     VARCHAR(255) NOT NULL,

    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT fk_restaurant_image_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
) ENGINE=InnoDB;
