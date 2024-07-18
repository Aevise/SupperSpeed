CREATE TABLE user_rating
(
    user_rating_id         SERIAL                   NOT NULL,
    food_rating            INT                      NOT NULL CHECK (food_rating BETWEEN 1 AND 5),
    delivery_rating        INT                      NOT NULL CHECK (delivery_rating BETWEEN 1 AND 5),
    rating_date_time       TIMESTAMP WITH TIME ZONE NOT NULL,
    description            VARCHAR(256),
    restaurant_response_id INT UNIQUE,
    PRIMARY KEY (user_rating_id),
    CONSTRAINT fk_restaurant_response_rating
        FOREIGN KEY (restaurant_response_id)
            REFERENCES restaurant_response (restaurant_response_id)
);