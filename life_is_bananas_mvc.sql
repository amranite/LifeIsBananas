CREATE DATABASE IF NOT EXISTS life_is_bananas_mvc
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE life_is_bananas_mvc;

CREATE TABLE IF NOT EXISTS raw_material (
  id BIGINT NOT NULL AUTO_INCREMENT,
  code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  organic_certified BIT(1) NOT NULL,
  price DOUBLE NOT NULL,
  amount DOUBLE NOT NULL,
  unit VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_raw_material_code (code)
);

INSERT INTO raw_material (code, name, organic_certified, price, amount, unit)
VALUES
  ('RM-001', 'Banana Puree', b'1', 4.25, 25.00, 'KG'),
  ('RM-002', 'Cocoa Powder', b'0', 7.90, 10.00, 'KG')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  organic_certified = VALUES(organic_certified),
  price = VALUES(price),
  amount = VALUES(amount),
  unit = VALUES(unit);
