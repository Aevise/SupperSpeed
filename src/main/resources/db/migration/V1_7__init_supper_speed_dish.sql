CREATE TABLE dish
(
    dish_id          SERIAL        NOT NULL,
    restaurant_id    INT           NOT NULL,
    dish_category_id INT           NOT NULL,
    name             VARCHAR(32)   NOT NULL,
    description      VARCHAR(128),
    price            NUMERIC(8, 2) NOT NULL,
    photo            VARCHAR(128),
    availability     BOOLEAN       NOT NULL,
    PRIMARY KEY (dish_id),
    CONSTRAINT fk_dish_restaurant_id
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (user_id),
    CONSTRAINT fk_dish_category_id
        FOREIGN KEY (dish_category_id)
            REFERENCES dish_category (dish_category_id)
);