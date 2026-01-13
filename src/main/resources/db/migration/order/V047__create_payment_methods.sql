CREATE TABLE payment_methods
(
    id         BIGSERIAL PRIMARY KEY,
    code       VARCHAR(50) UNIQUE                 NOT NULL, -- COD, VNPAY
    name       VARCHAR(100)                       NOT NULL,
    type       VARCHAR(20)                        NOT NULL, -- OFFLINE / ONLINE
    active     BOOLEAN DEFAULT TRUE               NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    deleted_at TIMESTAMP NULL,
    version    BIGINT    DEFAULT 0                NOT NULL,

    CONSTRAINT ck_payment_methods_type CHECK (type IN ('OFFLINE', 'ONLINE'))
);

CREATE INDEX idx_payment_methods_active ON payment_methods (active);
CREATE INDEX idx_payment_methods_type ON payment_methods (type);

