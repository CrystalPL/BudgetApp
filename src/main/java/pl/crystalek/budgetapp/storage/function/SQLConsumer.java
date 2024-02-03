package pl.crystalek.budgetapp.storage.function;

import java.sql.SQLException;

public interface SQLConsumer<T> {

    void accept(T connection) throws SQLException;
}
