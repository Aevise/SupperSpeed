CREATE TABLE menu
(
    menu_id     SERIAL      NOT NULL,
    name        VARCHAR(32) NOT NULL,
    description VARCHAR(128),
    PRIMARY KEY (menu_id)
);