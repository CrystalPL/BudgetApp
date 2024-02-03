package pl.crystalek.budgetapp.receipt;

import com.zaxxer.hikari.HikariDataSource;
import pl.crystalek.budgetapp.receipt.infra.MysqlReceiptRepository;
import pl.crystalek.budgetapp.receipt.infra.SqliteReceiptRepository;
import pl.crystalek.budgetapp.storage.StorageType;

public class ReceiptRepositoryFactory {

    public static ReceiptRepository createReceiptRepository(final HikariDataSource dataSource, final StorageType storageType) {
        switch (storageType) {
            case MYSQL -> {
                return new MysqlReceiptRepository(dataSource);
            }
            case LOCALE -> {
                return new SqliteReceiptRepository(dataSource);
            }
            default -> throw new IllegalStateException("Unexpected value: " + storageType);
        }
    }
}
