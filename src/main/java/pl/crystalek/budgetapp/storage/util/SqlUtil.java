package pl.crystalek.budgetapp.storage.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.storage.function.SQLConsumer;
import pl.crystalek.budgetapp.storage.function.SQLFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public abstract class SqlUtil {
    HikariDataSource database;

    protected void openConnection(final SQLConsumer<Connection> consumer) {
        try (final Connection connection = database.getConnection()) {
            consumer.accept(connection);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected <R> R openConnection(final SQLFunction<Connection, R> function) {
        try (final Connection connection = database.getConnection()) {
            return function.apply(connection);
        } catch (final SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    protected Boolean executeUpdate(final Connection connection, final String sql, final Object... params) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            completionStatement(statement, params);
            return statement.executeUpdate() == 1;
        }
    }

    protected Boolean executeUpdateAndOpenConnection(final String sql, final Object... params) {
        return openConnection(connection -> {
            return executeUpdate(connection, sql, params);
        });
    }

    protected <R> R executeQuery(final Connection connection, final String sql, final SQLFunction<ResultSet, R> consumer, final Object... params) throws SQLException {
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            completionStatement(statement, params);
            try (final ResultSet resultSet = statement.executeQuery()) {
                return consumer.apply(resultSet);
            }
        }
    }

    protected <R> R executeQueryAndOpenConnection(final String sql, final SQLFunction<ResultSet, R> consumer, final Object... params) {
        return openConnection(connection -> {
            return executeQuery(connection, sql, consumer, params);
        });
    }

    protected void completionStatement(final PreparedStatement statement, final Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }

    protected int getLastInsertId(final Connection connection) throws SQLException {
        final SQLFunction<ResultSet, Integer> lastInsertIdFunction = resultSet -> {
            resultSet.next();
            return resultSet.getInt("LAST_INSERT_ID()");
        };

        return executeQuery(connection, "SELECT LAST_INSERT_ID();", lastInsertIdFunction);
    }
}
