package pl.crystalek.budgetapp.user.infra;

import com.zaxxer.hikari.HikariDataSource;

public class MysqlUserRepository extends SqlUserRepository {
    private static final String USER_EXIST = "SELECT name, icon FROM users;";
    private static final String CREATE_USER = "INSERT INTO users(name, icon, sex) VALUES (?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE users SET name = ?, icon = ?, sex = ? WHERE id = ?;";
    private static final String GET_USERS = "SELECT * FROM users;";
    private static final String GET_USERS_DTO = "SELECT id, name FROM users;";
    private static final String USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS users
            (
                id   INTEGER PRIMARY KEY AUTO_INCREMENT UNIQUE NOT NULL,
                name TEXT                                      NOT NULL,
                icon BLOB                                      NOT NULL,
                sex  TEXT                                      NOT NULL
            );""";

    public MysqlUserRepository(final HikariDataSource hikariDataSource) {
        super(hikariDataSource, USER_EXIST, CREATE_USER, UPDATE_USER, GET_USERS, GET_USERS_DTO, USERS_TABLE);
    }
}
