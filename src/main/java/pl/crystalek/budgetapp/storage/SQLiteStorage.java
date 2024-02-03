package pl.crystalek.budgetapp.storage;

import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.IOException;

public class SQLiteStorage extends Storage {

    public SQLiteStorage(final StorageSettings storageSettings) {
        super(storageSettings);
    }

    @Override
    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (final ClassNotFoundException exception) {
            exception.printStackTrace();
            return false;
        }

        dataSource = new HikariDataSource();
        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSource.addDataSourceProperty("useServerPrepStmts", true);
        dataSource.setConnectionTestQuery("SELECT * FROM sqlite_master");

        final File localeStorageLocation = new File(storageSettings.getLocaleStorageLocation(), "data/data.db");
        if (!localeStorageLocation.exists()) {
            try {
                final File parentFile = localeStorageLocation.getParentFile();
                if (!parentFile.exists()) {
                    if (!parentFile.mkdir()) {
                        return false;
                    }
                }

                if (!localeStorageLocation.createNewFile()) {
                    return false;
                }
            } catch (final IOException exception) {
                exception.printStackTrace();
                return false;
            }
        }

        dataSource.setJdbcUrl(String.format("jdbc:sqlite:%s", localeStorageLocation.getAbsolutePath()));
        return true;
    }
}
