CREATE TABLE user_rating
(
    user_rating_id   SERIAL                   NOT NULL,
    order_id         INT                      NOT NULL,
    food_rating      INT                      NOT NULL,
    delivery_rating  INT                      NOT NULL,
    rating_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    description      VARCHAR(256),
    response_id      INT,
    PRIMARY KEY (order_id),
    CONSTRAINT fk_rating_order
        FOREIGN KEY (order_id)
            REFERENCES order (order_id),
    CONSTRAINT fk_restaurant_response_rating
        FOREIGN KEY (response_id)
            REFERENCES restaurant_response (restaurant_response_id)
);