ALTER TABLE restaurants
    ADD COLUMN slug VARCHAR(255) UNIQUE
        AFTER code;
