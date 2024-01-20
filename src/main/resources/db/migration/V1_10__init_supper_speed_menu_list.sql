CREATE TABLE menu_list
(
    menu_id INT NOT NULL,
    dish_id INT NOT NULL,
    PRIMARY KEY (menu_id, dish_id),
    CONSTRAINT fk_menu_list_menu
        FOREIGN KEY (menu_id)
            REFERENCES menu (menu_id),
    CONSTRAINT fk_dish_menu_list
        FOREIGN KEY (dish_id)
            REFERENCES dish (dish_id)
);