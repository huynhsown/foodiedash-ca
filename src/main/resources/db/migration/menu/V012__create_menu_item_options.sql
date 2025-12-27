CREATE TABLE IF NOT EXISTS menu_item_options
(
    id           BIGSERIAL    PRIMARY KEY,
    menu_item_id BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL,
    required     BOOLEAN      NOT NULL DEFAULT FALSE,
    min_value    INT,
    max_value    INT,

    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255),
    deleted_at   TIMESTAMP NULL,

    version      BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT fk_option_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items (id)
);
