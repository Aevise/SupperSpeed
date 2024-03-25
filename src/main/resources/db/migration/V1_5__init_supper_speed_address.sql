CREATE TABLE address
(
    address_id      SERIAL      NOT NULL,
    country         VARCHAR(32) ,
    city            VARCHAR(32) ,
    postal_code     VARCHAR(32) ,
    current_address VARCHAR(64) ,
    PRIMARY KEY (address_id)
);