CREATE TABLE user_role
(
    user_id INT NOT NULL UNIQUE,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT pk_user_user_role
        FOREIGN KEY (user_id)
            REFERENCES supper_user (user_id),
    CONSTRAINT pk_role_roles
        FOREIGN KEY (role_id)
            REFERENCES roles (role_id)
);