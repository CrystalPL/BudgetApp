package pl.crystalek.budgetapp.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StorageService {
    final StorageSettings storageSettings;
    @Getter
    Storage storage;

    public StorageService create() {
        final StorageType storageType = storageSettings.getStorageType();
        switch (storageType) {
            case MYSQL -> storage = new MYSQLStorage(storageSettings);
            case LOCALE -> storage = new SQLiteStorage(storageSettings);
        }

        return this;
    }
}
