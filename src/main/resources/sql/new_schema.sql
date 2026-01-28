ALTER TABLE dish
    ADD COLUMN IF NOT EXISTS selling_price NUMERIC(10, 2);

CREATE TABLE IF NOT EXISTS dish_ingredient
(
    id                SERIAL PRIMARY KEY,
    id_dish           INT NOT NULL REFERENCES dish (id) ON DELETE CASCADE,
    id_ingredient     INT NOT NULL REFERENCES ingredient (id) ON DELETE CASCADE,
    quantity_required NUMERIC(10, 2) NOT NULL,
    unit              VARCHAR(50) NOT NULL,
    UNIQUE (id_dish, id_ingredient)
);

ALTER TABLE ingredient
    DROP COLUMN IF EXISTS id_dish;

ALTER TABLE ingredient
    DROP COLUMN IF EXISTS required_quantity;

CREATE TYPE movement_type AS ENUM ('IN', 'OUT');

CREATE TABLE IF NOT EXISTS stock_movement
(
    id SERIAL PRIMARY KEY,
    id_ingredient INTEGER NOT NULL REFERENCES ingredient(id),
    quantity DECIMAL(10,3) NOT NULL,
    type movement_type NOT NULL,
    unit VARCHAR(10) DEFAULT 'KG',
    creation_datetime TIMESTAMP NOT NULL
);