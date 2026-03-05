CREATE TABLE IF NOT EXISTS menu_items
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    menu_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(12,2) NOT NULL,
    image_url TEXT,
    status VARCHAR(20) NOT NULL,

    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP NULL,

    version     BIGINT         NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT fk_menu_item_menu FOREIGN KEY (menu_id) REFERENCES menus(id)
) ENGINE=InnoDB;
