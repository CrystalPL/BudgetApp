package pl.crystalek.budgetapp.receipt.infra;

import com.zaxxer.hikari.HikariDataSource;


public class MysqlReceiptRepository extends SqlReceiptRepository {
    private static final String CREATE_RECEIPT_QUERY = "INSERT INTO receipts(shop_name, shopping_date, who_paid_id, settled) VALUES (?, ?, ?, ?);";
    private static final String CREATE_ITEM_QUERY = "INSERT INTO items(name, amount, price, category_id, money_division, receipt_id) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String LOAD_RECEIPTS_QUERY = """
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
            GROUP BY u.name, r.id, r.shop_name, r.shopping_date, r.settled;""";
    private static final String LOAD_RECEIPT_ITEMS_QUERY = """
            SELECT i.id AS item_id, i.name AS item_name, i.amount, i.price, i.money_division, c.id, c.name AS category_name
            FROM items i
                     INNER JOIN categories c ON i.category_id = c.id
            WHERE i.receipt_id = ?;""";
    private static final String UPDATE_RECEIPT = "UPDATE receipts SET shop_name = ?, shopping_date = ?, who_paid_id = ?, settled = ? WHERE id = ?;";
    private static final String UPDATE_RECEIPT_ITEM = "UPDATE items SET name = ?, amount = ?, price = ?, category_id = ?, money_division = ? WHERE id = ?;";
    private static final String DELETE_BY_ITEM_ID = "DELETE FROM items WHERE id = ?;";
    private static final String DELETE_ITEMS_BY_RECEIPT_ID = "DELETE FROM items WHERE receipt_id = ?;";
    private static final String DELETE_RECEIPT_BY_ID = "DELETE FROM receipts WHERE id = ?;";
    private static final String GET_SHOP_NAME_LIST = "SELECT shop_name AS name FROM receipts GROUP BY shop_name;";
    private static final String GET_PRODUCT_NAME_LIST = "SELECT name FROM items GROUP BY name;";
    private static final String RECEIPTS_TABLE = """
            CREATE TABLE IF NOT EXISTS receipts
            (
                id            INTEGER PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
                shop_name     VARCHAR(64)                               NOT NULL,
                shopping_date LONG                                      NOT NULL,
                who_paid_id   INTEGER                                   NOT NULL,
                settled       BOOLEAN                                   NOT NULL,
                FOREIGN KEY (who_paid_id) REFERENCES users (id)
            );""";
    private static final String ITEMS_TABLE = """
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
            );""";

    public MysqlReceiptRepository(final HikariDataSource database) {
        super(database, CREATE_RECEIPT_QUERY, CREATE_ITEM_QUERY, LOAD_RECEIPTS_QUERY, LOAD_RECEIPT_ITEMS_QUERY, UPDATE_RECEIPT,
                UPDATE_RECEIPT_ITEM, DELETE_BY_ITEM_ID, DELETE_ITEMS_BY_RECEIPT_ID, DELETE_RECEIPT_BY_ID, GET_SHOP_NAME_LIST,
                GET_PRODUCT_NAME_LIST, RECEIPTS_TABLE, ITEMS_TABLE
        );
    }
}
