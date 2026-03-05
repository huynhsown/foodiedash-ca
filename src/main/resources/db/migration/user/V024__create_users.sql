CREATE TABLE users
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,

    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(20),
    password   VARCHAR(255) NOT NULL,
    full_name  VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(500),
    status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMP NULL,

    version    BIGINT       NOT NULL DEFAULT 0,

    PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT uq_users_phone UNIQUE (phone)
) ENGINE = InnoDB;