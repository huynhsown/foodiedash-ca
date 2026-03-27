CREATE TABLE permissions
(
    id          BIGSERIAL    PRIMARY KEY,

    name        VARCHAR(100) NOT NULL,
    module      VARCHAR(50)  NOT NULL,
    description VARCHAR(255),

    CONSTRAINT uq_permissions_name UNIQUE (name)
);