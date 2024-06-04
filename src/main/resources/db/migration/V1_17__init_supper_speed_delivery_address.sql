CREATE TABLE delivery_address
(
    delivery_address_id SERIAL NOT NULL,
    country             VARCHAR(32),
    city                VARCHAR(64),
    district            varchar(32),
    postal_code         VARCHAR(32),
    street_name         VARCHAR(64),
    PRIMARY KEY (delivery_address_id)
);