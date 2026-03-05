CREATE TABLE IF NOT EXISTS restaurant_categories
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    icon_url    TEXT,
    description TEXT,

    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP NULL,

    version     BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id)
) ENGINE=InnoDB;
