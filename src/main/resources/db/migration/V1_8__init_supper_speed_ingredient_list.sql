CREATE TABLE ingredient_list
(
    ingredient_list_id SERIAL      NOT NULL,
    dish_id            INT         NOT NULL,
    ingredient_id      INT         NOT NULL,
    quantity           SMALLINT    NOT NULL,
    units              VARCHAR(16) NOT NULL,
    PRIMARY KEY (ingredient_list_id),
    CONSTRAINT fk_dish_ingredient_list_id
        FOREIGN KEY (dish_id)
            REFERENCES dish (dish_id),
    CONSTRAINT fk_dish_ingredient_id
        FOREIGN KEY (ingredient_id)
            REFERENCES ingredient (ingredient_id)
);