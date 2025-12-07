CREATE SCHEMA IF NOT EXISTS shopping_store;

SET search_path TO shopping_store;

CREATE TABLE IF NOT EXISTS products (
    id               UUID PRIMARY KEY,
    product_name     VARCHAR(250) NOT NULL,
    description      VARCHAR(2000),
    quantity_state   VARCHAR(50)  NOT NULL,
    product_state    VARCHAR(50)  NOT NULL,
    product_category VARCHAR(50)  NOT NULL,
    image_src        VARCHAR(50),
    price            NUMERIC
);