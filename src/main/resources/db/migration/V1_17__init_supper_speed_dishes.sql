CREATE TABLE dishes
(
    dish_list_id SERIAL NOT NULL,
    dish_id      INT    NOT NULL,
    quantity     INT    NOT NULL,
    PRIMARY KEY (dish_list_id),
    CONSTRAINT fk_dish_dishes
        FOREIGN KEY (dish_id)
            REFERENCES dish (dish_id)
);