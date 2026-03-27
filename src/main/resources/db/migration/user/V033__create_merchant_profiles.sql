CREATE TABLE merchant_profiles
(
    id                  BIGSERIAL    PRIMARY KEY,
    user_id             BIGINT       NOT NULL,
    business_name       VARCHAR(255),
    business_license    VARCHAR(255),
    tax_code            VARCHAR(50),
    bank_name           VARCHAR(100),
    bank_account        VARCHAR(50),
    bank_holder_name    VARCHAR(255),
    contact_email       VARCHAR(255),
    contact_phone       VARCHAR(20),

    verification_status VARCHAR(20)  NOT NULL DEFAULT 'PENDING',

    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP    NULL,

    version             BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT uq_merchant_user UNIQUE (user_id),
    CONSTRAINT fk_merchant_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);