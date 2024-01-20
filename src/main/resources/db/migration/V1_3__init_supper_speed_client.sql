CREATE TABLE client
(
    user_id              INT                      NOT NULL,
    email                VARCHAR(32)              NOT NULL,
    password             VARCHAR(128),
    active               BOOLEAN                  NOT NULL,
    creation_date_time   TIMESTAMP WITH TIME ZONE NOT NULL,
    last_login_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    phone                VARCHAR(32)              NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_client_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (user_id),
    CONSTRAINT fk_client_address_id
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)
);