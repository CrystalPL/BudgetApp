package pl.crystalek.budgetapp.storage;

import com.zaxxer.hikari.HikariDataSource;

public class MYSQLStorage extends Storage {

    public MYSQLStorage(final StorageSettings storageSettings) {
        super(storageSettings);
    }

    @Override
    public boolean connect() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://" + storageSettings.getDatabaseHostname() + ":" + storageSettings.getDatabasePort() + "/" + storageSettings.getDatabase() + "?allowPublicKeyRetrieval=true" + "&useSSL=true");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(storageSettings.getDatabaseUsername());
        dataSource.setPassword(storageSettings.getDatabasePassword());
        dataSource.setMaximumPoolSize(5);
        dataSource.setConnectionTimeout(30_000);

        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSource.addDataSourceProperty("useServerPrepStmts", true);

        return true;
    }
}
