CREATE TABLE permissions
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,

    name        VARCHAR(100) NOT NULL,
    module      VARCHAR(50)  NOT NULL,
    description VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT uq_permissions_name UNIQUE (name)
) ENGINE = InnoDB;