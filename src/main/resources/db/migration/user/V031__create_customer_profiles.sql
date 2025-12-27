CREATE TABLE customer_profiles
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT    NOT NULL,
    date_of_birth DATE,
    gender        VARCHAR(10),

    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP NULL,

    version       BIGINT    NOT NULL DEFAULT 0,
    CONSTRAINT uq_customer_user UNIQUE (user_id),
    CONSTRAINT fk_customer_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);