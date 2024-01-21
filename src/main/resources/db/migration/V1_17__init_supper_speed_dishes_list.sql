CREATE TABLE dishes_list
(
    dish_id  INT NOT NULL,
    order_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (dish_id, order_id),
    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
            REFERENCES supper_order (order_id),
    CONSTRAINT fk_dish
        FOREIGN KEY (order_id)
            REFERENCES dish (dish_id)
);