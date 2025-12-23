CREATE SCHEMA IF NOT EXISTS delivery;

SET search_path TO delivery;

CREATE TABLE IF NOT EXISTS delivery (
    delivery_id  UUID        PRIMARY KEY,
    order_id     UUID        NOT NULL,
    state        VARCHAR(50) NOT NULL,
    total_weight NUMERIC,
    total_volume NUMERIC,
    fragile      BOOLEAN,
    from_address VARCHAR(500),
    to_address   VARCHAR(500)
);
