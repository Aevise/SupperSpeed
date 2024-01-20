CREATE TABLE restaurant_response
(
    restaurant_response_id SERIAL                   NOT NULL,
    description            VARCHAR(16)              NOT NULL,
    response_date_time     TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (restaurant_response_id)
);