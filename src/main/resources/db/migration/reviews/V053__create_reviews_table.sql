CREATE TABLE reviews
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT      NOT NULL,
    customer_id    BIGINT      NOT NULL,
    restaurant_id  BIGINT      NOT NULL,
    rating         INTEGER     NOT NULL
        CHECK (rating BETWEEN 1 AND 5),
    comment        TEXT,
    images         JSONB,
    merchant_reply TEXT,
    replied_at     TIMESTAMP,
    status         VARCHAR(20) NOT NULL,

    created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(255),
    updated_by     VARCHAR(255),
    deleted_at     TIMESTAMP NULL,

    version        BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT fk_review_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id),

    CONSTRAINT fk_review_customer
        FOREIGN KEY (customer_id)
            REFERENCES users (id),

    CONSTRAINT fk_review_restaurant
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurants (id),

    CONSTRAINT uq_review_order
        UNIQUE (order_id, customer_id)
);

CREATE INDEX idx_reviews_customer_id ON reviews(customer_id);
CREATE INDEX idx_reviews_restaurant_id ON reviews(restaurant_id);
CREATE INDEX idx_reviews_order_id ON reviews(order_id);