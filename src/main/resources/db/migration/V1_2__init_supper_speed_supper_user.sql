CREATE TABLE supper_user
(
    user_id              SERIAL                   NOT NULL,
    email                VARCHAR(255)             NOT NULL UNIQUE,
    password             VARCHAR(128)             NOT NULL,
    active               BOOLEAN                  NOT NULL DEFAULT FALSE,
    creation_date_time   TIMESTAMP WITH TIME ZONE NOT NULL,
    last_login_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    role_id              INTEGER                  NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT pk_user_role
        FOREIGN KEY (role_id)
            REFERENCES roles (role_id)
);
