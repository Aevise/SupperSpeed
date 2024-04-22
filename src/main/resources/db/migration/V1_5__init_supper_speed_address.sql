CREATE TABLE address
(
    address_id      SERIAL      NOT NULL,
    country         VARCHAR(32) ,
    city            VARCHAR(32) ,
    postal_code     VARCHAR(32) ,
    street_name VARCHAR(64) ,
    building_number VARCHAR(12),
    local_number INT,
    PRIMARY KEY (address_id)
);