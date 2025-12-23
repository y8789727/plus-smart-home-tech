CREATE SCHEMA IF NOT EXISTS payment;

SET search_path TO payment;

CREATE TABLE IF NOT EXISTS payment (
    payment_id     UUID    PRIMARY KEY,
    total_payment  NUMERIC NOT NULL,
    delivery_total NUMERIC NOT NULL,
    product_price  NUMERIC NOT NULL,
    order_id       UUID,
    state          VARCHAR(50) NOT NULL
);