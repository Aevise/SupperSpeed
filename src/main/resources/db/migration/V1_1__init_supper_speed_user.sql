CREATE TABLE user
(
    user_id                 SERIAL                      NOT NULL,
    email                   VARCHAR(32)                 NOT NULL,
    password                VARCHAR(128)                NOT NULL,
    active                  BOOLEAN                     NOT NULL,
    phone                   VARCHAR(32)                         ,
    creation_date_time      TIMESTAMP WITH TIME ZONE    NOT NULL,
    last_login_date_time    TIMESTAMP WITH TIME ZONE    NOT NULL,
    PRIMARY KEY (user_id)
);
