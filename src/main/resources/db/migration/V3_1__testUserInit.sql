INSERT INTO address(address_id, country, city, postal_code, current_address)
VALUES (1, 'Poland', 'Chelm', '22-100', 'hehe 12');

INSERT INTO address(address_id, country, city, postal_code, current_address)
VALUES (2, 'Poland', 'Lublin', '22-100', 'hehe 12');

INSERT INTO address(address_id, country, city, postal_code, current_address)
VALUES (3, 'Poland', 'Lublin', '22-100', 'hehe 12');

INSERT INTO address(address_id, country, city, postal_code, current_address)
VALUES (4, 'Poland', 'Kreska', '22-100', 'hehe 12');

INSERT INTO supper_user(user_id, email, password, active, creation_date_time, last_login_date_time, role_id)
VALUES (1, 'test1@test.com', '$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42', true, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 2);

INSERT INTO supper_user(user_id, email, password, active, creation_date_time, last_login_date_time, role_id)
VALUES (2, 'test2@test.com', '$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42', true, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 2);

INSERT INTO supper_user(user_id, email, password, active, creation_date_time, last_login_date_time, role_id)
VALUES (3, 'test3@test.com', '$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42', true, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 2);

INSERT INTO supper_user(user_id, email, password, active, creation_date_time, last_login_date_time, role_id)
VALUES (4, 'test4@test.com', '$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42', true, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 1);


INSERT INTO restaurant(supper_restaurant_id, restaurant_name, address_id, open_hour, close_hour, cuisine_id)
VALUES (1, 'test1', 1, CURRENT_TIME, CURRENT_TIME, 5);
INSERT INTO restaurant(supper_restaurant_id, restaurant_name, address_id, open_hour, close_hour, cuisine_id)
VALUES (2, 'test2', 2, CURRENT_TIME, CURRENT_TIME, 5);
INSERT INTO restaurant(supper_restaurant_id, restaurant_name, address_id, open_hour, close_hour, cuisine_id)
VALUES (3, 'test3', 3, CURRENT_TIME, CURRENT_TIME, 9);
INSERT INTO client(supper_client_id, name, surname, phone, address_id)
VALUES (4, 'test1', 'test2', '+48 123 456 789', 4);

INSERT INTO dish_category(dish_category_id, restaurant_id, category_name)
VALUES (1, 3, 'Dania rybne');
INSERT INTO dish_category(dish_category_id, restaurant_id, category_name)
VALUES (2, 3, 'Dania mięsne');
INSERT INTO dish_category(dish_category_id, restaurant_id, category_name)
VALUES (3, 3, 'Dania wegańskie');
INSERT INTO dish_category(dish_category_id, restaurant_id, category_name)
VALUES (4, 2, 'Dania wegańskie');

INSERT INTO dish(dish_id, restaurant_id, dish_category_id, name, description, price, photo, availability)
VALUES (1, 3, 1, 'Ryba po grecku', 'Jakas tam rybka po grecku', '12.3', NULL, true);

INSERT INTO dish(dish_id, restaurant_id, dish_category_id, name, description, price, photo, availability)
VALUES (2, 3, 1, 'Mintaj', 'Jaki jest każdy wie', '10.3', NULL, true);

INSERT INTO dish(dish_id, restaurant_id, dish_category_id, name, description, price, photo, availability)
VALUES (3, 3, 2, 'Jeleń', 'Prosto z drogi', '35.3', NULL, true);

INSERT INTO dish(dish_id, restaurant_id, dish_category_id, name, description, price, photo, availability)
VALUES (4, 3, 2, 'Dzik', 'Ten już nie jest ani dziki ani zły', '45.3', NULL, true);

INSERT INTO dish(dish_id, restaurant_id, dish_category_id, name, description, price, photo, availability)
VALUES (5, 3, 3, 'Trawa', 'Je jom krowa', '1.3', NULL, true);

