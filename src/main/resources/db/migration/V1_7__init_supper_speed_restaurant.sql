CREATE TABLE restaurant
(
    supper_restaurant_id INT         NOT NULL,
    restaurant_id        SERIAL      NOT NULL,
    restaurant_name      VARCHAR(32) NOT NULL,
    address_id           INT,
    phone                VARCHAR(32),
    open_hour            TIME        NOT NULL,
    close_hour           TIME        NOT NULL,
    image_id             INT,
    cuisine_id           INT,
    is_shown              BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (supper_restaurant_id),
    CONSTRAINT fk_restaurant_user_id
        FOREIGN KEY (supper_restaurant_id)
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