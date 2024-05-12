CREATE TABLE restaurant
(
    restaurant_id        SERIAL      NOT NULL,
    user_id              INT,
    address_id           INT,
    image_id             INT,
    cuisine_id           INT,
    restaurant_name      VARCHAR(32) NOT NULL,
    phone                VARCHAR(32),
    open_hour            TIME        NOT NULL,
    close_hour           TIME        NOT NULL,
    is_shown             BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (restaurant_id),
    CONSTRAINT fk_restaurant_user_id
        FOREIGN KEY (user_id)
            REFERENCES supper_user (user_id),
    CONSTRAINT fk_restaurant_address_id
        FOREIGN KEY (address_id)
            REFERENCES address (address_id),
    CONSTRAINT fk_restaurant_image
        FOREIGN KEY (image_id)
            REFERENCES image (image_id),
    CONSTRAINT fk_restaurant_cuisine
        FOREIGN KEY (cuisine_id)
            REFERENCES cuisine (cuisine_id)
);