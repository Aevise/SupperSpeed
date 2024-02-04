CREATE TABLE address
(
    address_id      SERIAL      NOT NULL,
    country         VARCHAR(32) NOT NULL,
    city            VARCHAR(32) NOT NULL,
    postal_code     VARCHAR(32) NOT NULL,
    current_address VARCHAR(64) NOT NULL,
    PRIMARY KEY (address_id)
);