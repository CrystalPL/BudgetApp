package pl.crystalek.budgetapp.user;

import com.zaxxer.hikari.HikariDataSource;
import pl.crystalek.budgetapp.storage.StorageType;
import pl.crystalek.budgetapp.user.infra.MysqlUserRepository;
import pl.crystalek.budgetapp.user.infra.SqliteUserRepository;

public class UserRepositoryFactory {

    public static UserRepository createUserRepository(final HikariDataSource dataSource, final StorageType storageType) {
        switch (storageType) {
            case MYSQL -> {
                return new MysqlUserRepository(dataSource);
            }
            case LOCALE -> {
                return new SqliteUserRepository(dataSource);
            }
            default -> throw new IllegalStateException("Unexpected value: " + storageType);
        }
    }
}
