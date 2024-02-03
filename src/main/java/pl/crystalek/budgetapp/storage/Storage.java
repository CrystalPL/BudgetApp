package pl.crystalek.budgetapp.storage;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class Storage {
    final StorageSettings storageSettings;
    HikariDataSource dataSource;

    public abstract boolean connect();

    public boolean testConnection() {
        try (

                final Connection connection = dataSource.getConnection()
        ) {
            return true;
        } catch (final SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
