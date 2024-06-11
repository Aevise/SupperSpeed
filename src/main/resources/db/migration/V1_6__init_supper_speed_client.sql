CREATE TABLE client
(
    client_id  SERIAL      NOT NULL,
    user_id    INT         NOT NULL,
    name       VARCHAR(32) NOT NULL,
    surname    VARCHAR(32),
    phone      VARCHAR(32),
    address_id INT,
    PRIMARY KEY (client_id),
    CONSTRAINT fk_client_user_id
        FOREIGN KEY (user_id)
            REFERENCES supper_user (user_id),
    CONSTRAINT fk_client_address_id
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)
);