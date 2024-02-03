CREATE TABLE IF NOT EXISTS categories
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
    name      VARCHAR(64) UNIQUE                        NOT NULL,
    color_hex CHAR(7) UNIQUE                            NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
    name TEXT NOT NULL,
    icon BLOB NOT NULL,
    sex  TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS receipts
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
    shop_name     VARCHAR(64)                               NOT NULL,
    shopping_date LONG                                      NOT NULL,
    who_paid_id   INTEGER                                   NOT NULL,
    settled       BOOLEAN                                   NOT NULL,
    FOREIGN KEY (who_paid_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id             INTEGER PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
    name           VARCHAR(64)                               NOT NULL,
    amount         DOUBLE                                    NOT NULL,
    price          DOUBLE                                    NOT NULL,
    category_id    INTEGER                                   NOT NULL,
    money_division DOUBLE                                    NOT NULL,
    receipt_id     INTEGER                                   NOT NULl,
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (receipt_id) REFERENCES receipts (id)
);

SELECT name, icon FROM users;

INSERT INTO users(name, icon, sex) VALUES (?, ?, ?);

UPDATE users SET name = ?, icon = ?, sex = ? WHERE id = ?;

SELECT * FROM users;



INSERT INTO test_receipts(shop_name, shopping_date, who_paid_id, settled)
VALUES (?, ?, ?, ?);

INSERT INTO test_items(name, amount, price, category_id, money_division, receipt_id)
VALUES (?, ?, ?, ?, ?, ?);

UPDATE test_receipts
SET shop_name     = ?,
    shopping_date = ?,
    who_paid_id   = ?,
    settled       = ?
WHERE id = ?;

UPDATE test_items
SET name           = ?,
    amount         = ?,
    price          = ?,
    category_id    = ?,
    money_division = ?
WHERE id = ?;



SELECT u.name                            AS user_name,
       r.id                              AS receipt_id,
       r.shop_name,
       r.shopping_date,
       r.settled,
       ROUND(SUM(i.amount * i.price), 2) AS amount_for_purchase
FROM users u
         INNER JOIN test_receipts r ON u.id = r.who_paid_id
         LEFT JOIN test_items i ON r.id = i.receipt_id
GROUP BY u.name, r.id, r.shop_name, r.shopping_date, r.settled;

SELECT shop_name
FROM test_receipts
GROUP BY shop_name;

SELECT i.id AS item_id, i.name AS item_name, i.amount, i.price, c.id, c.category_name, c.color_hex
FROM test_items i
         INNER JOIN test_categories c ON i.category_id = c.id
WHERE i.receipt_id = ?;

DELETE
FROM test_items
WHERE id = ?;

DELETE
FROM test_items
WHERE receipt_id = ?;

DELETE
FROM test_receipts
WHERE id = ?;

SELECT i.id   AS item_id,
       i.name AS item_name,
       i.amount,
       i.price,
       i.money_division,
       c.category_name
FROM test_items i
         JOIN test_categories c on i.category_id = c.id
WHERE receipt_id = ?;

INSERT INTO test_items(name, amount, price, category_id, money_division, receipt_id)
VALUES (?, ?, ?, ?, ?, ?);

SELECT r.id AS receipt_id, r.shop_name, r.shopping_date, u.name AS user_name
FROM test_receipts r
         INNER JOIN users u ON r.who_paid_id = u.id;

SELECT name, amount, price, money_division, category_id
FROM test_items
WHERE receipt_id = ?;

INSERT INTO test_receipts(shop_name, shopping_date, who_paid_id, settled)
VALUES (?, ?, ?, ?);



INSERT INTO test_categories(category_name, color_hex)
VALUES (?, ?);

SELECT *
FROM test_categories;

UPDATE test_categories
SET category_name = ?,
    color_hex     = ?
WHERE id = ?;

SELECT EXISTS (SELECT 1
               FROM test_categories
               WHERE LOWER(category_name) = LOWER(?) OR LOWER(color_hex) = LOWER(?)) AS exist;



INSERT INTO items(name, amount, price, category_id, money_division, receipt_id) VALUES (?, ?, ?, ?, ?, ?);


SELECT u.name                            AS user_name,
       u.id                              AS user_id,
       r.id                              AS receipt_id,
       r.shop_name,
       r.shopping_date,
       r.settled,
       ROUND(SUM(i.amount * i.price), 2) AS amount_for_purchase
FROM users u
         INNER JOIN receipts r ON u.id = r.who_paid_id
         LEFT JOIN items i ON r.id = i.receipt_id
GROUP BY u.name, r.id, r.shop_name, r.shopping_date, r.settled;

SELECT i.id AS item_id, i.name AS item_name, i.amount, i.price, i.money_division, c.id, c.name AS category_name
FROM items i
         INNER JOIN categories c ON i.category_id = c.id
WHERE i.receipt_id = ?;



SELECT i.name                                               AS item_name,
       u.name                                               AS user_name,
       ROUND(SUM(i.amount * i.price * i.money_division), 2) AS amount_to_return,
       ROUND(SUM(i.amount * i.price), 2)                    AS amount_to_purchase
FROM items i
         JOIN categories c ON c.id = i.category_id
         JOIN receipts r ON r.id = i.receipt_id
         JOIN users u ON u.id = r.who_paid_id
WHERE shopping_date >= 1696156800000
  AND shopping_date <= 1698724800000
GROUP BY item_name, user_name
HAVING user_name = 'Jasio';


SELECT u.name                                               AS user_name,
       ROUND(SUM(i.amount * i.price * i.money_division), 2) AS amount_to_return,
       ROUND(SUM(i.amount * i.price), 2)                    AS amount_to_purchase
FROM items i
         JOIN receipts r ON r.id = i.receipt_id
         JOIN users u ON u.id = r.who_paid_id
WHERE shopping_date >= 1696156800000
  AND shopping_date <= 1698724800000
GROUP BY user_name;

SELECT i.name AS item_name, amount, price, money_division, u.name AS user_name
FROM items i
         JOIN receipts r ON r.id = i.receipt_id
         JOIN users u ON u.id = r.who_paid_id
WHERE shopping_date >= 1696156800000
  AND shopping_date <= 1698724800000;


SELECT u.name                                               AS user_name,
       ROUND(SUM(i.amount * i.price * i.money_division), 2) AS amount_to_return,
       ROUND(SUM(i.amount * i.price), 2)                    AS amount_to_purchase
FROM items i
         JOIN receipts r ON r.id = i.receipt_id
         JOIN users u ON u.id = r.who_paid_id
         JOIN categories c ON c.id = i.category_id
# WHERE shopping_date >= 1698714061000
WHERE settled = false
GROUP BY user_name;

SELECT COUNT(*)
FROM receipts
WHERE settled = FALSE;

UPDATE receipts
SET settled = TRUE
WHERE settled = false;

