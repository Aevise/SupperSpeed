CREATE TABLE ingredient
(
    ingredient_id SERIAL      NOT NULL,
    name          VARCHAR(64) NOT NULL,
    allergic      BOOLEAN     NOT NULL,
    PRIMARY KEY (ingredient_id)
);