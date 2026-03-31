CREATE TABLE notifications
(
    id               BIGSERIAL PRIMARY KEY,

    recipient_user_id BIGINT      NOT NULL,
    recipient_role    VARCHAR(20) NOT NULL,
    type              VARCHAR(50) NOT NULL,

    title_key        VARCHAR(100) NOT NULL,
    body_key         VARCHAR(100) NOT NULL,
    payload          TEXT,

    dedupe_key       VARCHAR(200) NOT NULL,
    read_at          TIMESTAMP NULL,

    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by       VARCHAR(255) NULL,
    updated_by       VARCHAR(255) NULL,
    deleted_at       TIMESTAMP NULL,

    version          BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT uq_notification_recipient_type_dedupe
        UNIQUE (recipient_user_id, recipient_role, type, dedupe_key)
);

CREATE INDEX idx_notifications_recipient_created_at
    ON notifications (recipient_user_id, created_at DESC);

