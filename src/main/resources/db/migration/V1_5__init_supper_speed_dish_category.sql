CREATE TABLE dish_category
(
    dish_category_id SERIAL      NOT NULL,
    restaurant_id    INT         NOT NULL,
    category_name    VARCHAR(32) NOT NULL,
    PRIMARY KEY (dish_category_id),
    CONSTRAINT fk_restaurant_dish_category_id
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (user_id)
);