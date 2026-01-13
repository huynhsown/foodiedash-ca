INSERT INTO payment_methods (code, name, type, active, created_at, updated_at, version)
VALUES ('COD', 'Cash on Delivery', 'OFFLINE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
ON CONFLICT (code) DO NOTHING;

INSERT INTO payment_methods (code, name, type, active, created_at, updated_at, version)
VALUES ('VNPAY', 'VNPay', 'ONLINE', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
ON CONFLICT (code) DO NOTHING;

