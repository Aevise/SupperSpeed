CREATE TABLE restaurant
(
    user_id         INT         NOT NULL,
    restaurant_name VARCHAR(32) NOT NULL,
    address_id      INT         NOT NULL,
    open_hour       TIME        NOT NULL,
    close_hour      TIME        NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_restaurant_user_id
        FOREIGN KEY (user_id)
            REFERENCES supper_user (user_id),
    CONSTRAINT fk_restaurant_address_id
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)
);