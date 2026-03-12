CREATE TABLE IF NOT EXISTS restaurants
(
    id          BIGSERIAL      PRIMARY KEY,
    code        VARCHAR(25)    NOT NULL UNIQUE,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    address     VARCHAR(255)   NOT NULL,
    phone       VARCHAR(255)   NOT NULL,
    lat         DECIMAL(10, 6) NOT NULL,
    lng         DECIMAL(10, 6) NOT NULL,
    status      VARCHAR(255)   NOT NULL,

    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP NULL,

    version     BIGINT         NOT NULL DEFAULT 0
);
