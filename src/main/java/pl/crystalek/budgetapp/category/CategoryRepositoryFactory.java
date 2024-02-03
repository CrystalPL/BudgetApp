package pl.crystalek.budgetapp.category;

import com.zaxxer.hikari.HikariDataSource;
import pl.crystalek.budgetapp.category.infra.MysqlCategoryRepository;
import pl.crystalek.budgetapp.category.infra.SqliteCategoryRepository;
import pl.crystalek.budgetapp.storage.StorageType;

public class CategoryRepositoryFactory {

    public static CategoryRepository createCategoryRepository(final HikariDataSource dataSource, final StorageType storageType) {
        switch (storageType) {
            case MYSQL -> {
                return new MysqlCategoryRepository(dataSource);
            }
            case LOCALE -> {
                return new SqliteCategoryRepository(dataSource);
            }
            default -> throw new IllegalStateException("Unexpected value: " + storageType);
        }
    }
}
