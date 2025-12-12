CREATE TABLE promotions
(
    id                  BIGSERIAL PRIMARY KEY,

    code                VARCHAR(50)    NOT NULL UNIQUE,
    name                VARCHAR(255)   NOT NULL,

    type                VARCHAR(30)    NOT NULL,
    value               DECIMAL(12, 2) NOT NULL,

    min_order_amount    DECIMAL(12, 2)          DEFAULT 0,
    max_discount_amount DECIMAL(12, 2),

    start_at            TIMESTAMP      NOT NULL,
    end_at              TIMESTAMP      NOT NULL,

    total_usage_limit   INT,
    per_user_limit      INT,

    status              VARCHAR(20)    NOT NULL,

    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP      NULL,

    version             BIGINT         NOT NULL DEFAULT 0
);
