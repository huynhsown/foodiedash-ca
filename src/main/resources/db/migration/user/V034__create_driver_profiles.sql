CREATE TABLE driver_profiles
(
    id                  BIGSERIAL   PRIMARY KEY,
    user_id             BIGINT      NOT NULL,
    id_card_number      VARCHAR(20),
    id_card_front_url   VARCHAR(500),
    id_card_back_url    VARCHAR(500),
    license_number      VARCHAR(50),
    vehicle_type        VARCHAR(30) NOT NULL DEFAULT 'MOTORBIKE',
    vehicle_plate       VARCHAR(20),
    driver_license_url  VARCHAR(500),
    bank_name           VARCHAR(100),
    bank_account        VARCHAR(50),
    bank_holder_name    VARCHAR(255),
    current_lat         DECIMAL(10, 6),
    current_lng         DECIMAL(10, 6),
    is_online           BOOLEAN     NOT NULL DEFAULT FALSE,
    verification_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    created_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP   NULL,

    version             BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT uq_driver_user UNIQUE (user_id),
    CONSTRAINT fk_driver_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);