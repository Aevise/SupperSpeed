CREATE TABLE delivery_address_list
(
    restaurant_id       INTEGER NOT NULL,
    delivery_address_id INTEGER NOT NULL,

    PRIMARY KEY (restaurant_id, delivery_address_id),
    CONSTRAINT fk_restaurant_delivery_list
        FOREIGN KEY (restaurant_id)
            REFERENCES restaurant (restaurant_id),
    CONSTRAINT fk_deliver_address_delivery_list
        FOREIGN KEY (delivery_address_id)
            REFERENCES delivery_address (delivery_address_id)
);