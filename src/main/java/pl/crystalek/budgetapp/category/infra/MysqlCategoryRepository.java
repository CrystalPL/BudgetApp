package pl.crystalek.budgetapp.category.infra;

import com.zaxxer.hikari.HikariDataSource;

public class MysqlCategoryRepository extends SqlCategoryRepository {
    private static final String LOAD_CATEGORIES_QUERY = "SELECT * FROM categories;";
    private static final String LOAD_CATEGORIES_DTO_QUERY = "SELECT id, name FROM categories;";
    private static final String CREATE_CATEGORY_QUERY = "INSERT INTO categories(name, color_hex) VALUES (?, ?);";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name = ?, color_hex = ? WHERE id = ?;";
    private static final String CATEGORIES_TABLE = """
            CREATE TABLE IF NOT EXISTS categories
            (
                id        INTEGER PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
                name      VARCHAR(64) UNIQUE                        NOT NULL,
                color_hex CHAR(7) UNIQUE                            NOT NULL
            );""";

    public MysqlCategoryRepository(final HikariDataSource hikariDataSource) {
        super(hikariDataSource, LOAD_CATEGORIES_QUERY, LOAD_CATEGORIES_DTO_QUERY, CREATE_CATEGORY_QUERY, UPDATE_CATEGORY, CATEGORIES_TABLE);
    }
}
