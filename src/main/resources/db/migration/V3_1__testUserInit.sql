INSERT INTO address(country, city, postal_code, street_name, building_number)
VALUES ('Poland', 'Chelm', '22-100', 'hehe', 1);
INSERT INTO address(country, city, postal_code, street_name, building_number)
VALUES ('Poland', 'Lublin', '22-100', 'not hehe', 2);
INSERT INTO address(country, city, postal_code, street_name, building_number)
VALUES ('Poland', 'Lublin', '22-100', 'Jaskrawa', 3);
INSERT INTO address(country, city, postal_code, street_name, building_number)
VALUES ('Poland', 'Kreska', '22-100', 'Domowa', 4);

INSERT INTO supper_user(email, password, active, creation_date_time, last_login_date_time, role_id)
VALUES ('user1@user.com', '$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42', true, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 2);
INSERT INTO supper_user(email, password, active, creation_date_time, last_login_date_time, role_id)
VALUES ('user2@user.com', '$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42', true, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 2);
INSERT INTO supper_user(email, password, active, creation_date_time, last_login_date_time, role_id)
VALUES ('user3@user.com', '$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42', true, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 2);
INSERT INTO supper_user(email, password, active, creation_date_time, last_login_date_time, role_id)
VALUES ('user4@user.com', '$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42', true, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 1);


INSERT INTO restaurant(user_id, restaurant_name, address_id, open_hour, close_hour, cuisine_id, is_shown)
VALUES (1, 'restaurant1', 1, CURRENT_TIME, CURRENT_TIME, 5, true);
INSERT INTO restaurant(user_id, restaurant_name, address_id, open_hour, close_hour, cuisine_id, is_shown)
VALUES (2, 'restaurant2', 2, CURRENT_TIME, CURRENT_TIME, 5, true);
INSERT INTO restaurant(user_id, restaurant_name, address_id, open_hour, close_hour, cuisine_id, is_shown)
VALUES (3, 'restaurant3', 3, CURRENT_TIME, CURRENT_TIME, 9, true);
INSERT INTO client(user_id, name, surname, phone, address_id)
VALUES (4, 'client1', 'client2', '+48 123 456 789', 4);

INSERT INTO dish_category(restaurant_id, category_name)
VALUES (3, 'Dania rybne');
INSERT INTO dish_category(restaurant_id, category_name)
VALUES (3, 'Dania mięsne');
INSERT INTO dish_category(restaurant_id, category_name)
VALUES (3, 'Dania wegańskie');
INSERT INTO dish_category(restaurant_id, category_name)
VALUES (2, 'Dania wegańskie');

INSERT INTO dish(restaurant_id, dish_category_id, name, description, price, image_id, availability)
VALUES (3, 1, 'Ryba po grecku', 'Jakas tam rybka po grecku', '12.3', NULL, true);
INSERT INTO dish(restaurant_id, dish_category_id, name, description, price, image_id, availability)
VALUES (3, 1, 'Mintaj', 'Jaki jest każdy wie', '10.3', NULL, true);
INSERT INTO dish(restaurant_id, dish_category_id, name, description, price, image_id, availability)
VALUES (3, 2, 'Jeleń', 'Prosto z drogi', '35.3', NULL, true);
INSERT INTO dish(restaurant_id, dish_category_id, name, description, price, image_id, availability)
VALUES (3, 2, 'Dzik', 'Ten już nie jest ani dziki ani zły', '45.3', NULL, true);
INSERT INTO dish(restaurant_id, dish_category_id, name, description, price, image_id, availability)
VALUES (3, 3, 'Trawa', 'Je jom krowa', '1.3', NULL, true);

INSERT INTO user_rating(food_rating, delivery_rating, rating_date_time, description)
VALUES (2, 3, CURRENT_TIMESTAMP, 'testUserRating1');

INSERT INTO supper_order(client_id, restaurant_id, order_date_time, status_id)
VALUES (1, 3, CURRENT_TIMESTAMP, 1);
INSERT INTO supper_order(client_id, restaurant_id, order_date_time, status_id, rating_id)
VALUES (1, 3, CURRENT_TIMESTAMP, 2, 1);
INSERT INTO supper_order(client_id, restaurant_id, order_date_time, status_id)
VALUES (1, 3, CURRENT_TIMESTAMP, 3);
INSERT INTO supper_order(client_id, restaurant_id, order_date_time, status_id)
VALUES (1, 3, CURRENT_TIMESTAMP, 4);
INSERT INTO supper_order(client_id, restaurant_id, order_date_time, status_id)
VALUES (1, 3, CURRENT_TIMESTAMP, 5);
INSERT INTO supper_order(client_id, restaurant_id, order_date_time, status_id)
VALUES (1, 3, CURRENT_TIMESTAMP, 5);
INSERT INTO supper_order(client_id, restaurant_id, order_date_time, status_id)
VALUES (1, 2, CURRENT_TIMESTAMP, 5);

INSERT INTO dishes_list(dish_id, order_id, quantity)
VALUES (1, 1, 3);
INSERT INTO dishes_list(dish_id, order_id, quantity)
VALUES (2, 1, 2);
INSERT INTO dishes_list(dish_id, order_id, quantity)
VALUES (3, 2, 1);