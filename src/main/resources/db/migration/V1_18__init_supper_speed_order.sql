CREATE TABLE order
(
    order_id        SERIAL                   NOT NULL,
    client_id       INT                      NOT NULL,
    restaurant_id   INT                      NOT NULL,
    dishes_id       INT                      NOT NULL,
    order_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status_id       INT                      NOT NULL,
    rating_id       INT,
    PRIMARY KEY (order_id),
    CONSTRAINT fk_client_order
        FOREIGN KEY (client_id)
            REFERENCES client (user_id),
    CONSTRAINT fk_restaurant_order
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (user_id),
    CONSTRAINT fk_rating_order
        FOREIGN KEY (rating_id)
            REFERENCES user_rating (user_rating_id),
    CONSTRAINT fk_dishes_order
        FOREIGN KEY (dishes_id)
            REFERENCES dishes (dish_list_id)
);