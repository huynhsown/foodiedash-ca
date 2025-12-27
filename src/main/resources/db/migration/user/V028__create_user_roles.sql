CREATE TABLE user_roles
(
    user_id   BIGINT      NOT NULL,
    role_name VARCHAR(50) NOT NULL,

    PRIMARY KEY (user_id, role_name),

    CONSTRAINT fk_ur_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_ur_role
        FOREIGN KEY (role_name) REFERENCES roles (name)
            ON DELETE CASCADE
);