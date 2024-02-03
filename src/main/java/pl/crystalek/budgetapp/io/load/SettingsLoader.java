package pl.crystalek.budgetapp.io.load;

import lombok.experimental.UtilityClass;
import pl.crystalek.budgetapp.storage.StorageSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

@UtilityClass
public class SettingsLoader {

    public Optional<StorageSettings> loadSettings(final File fileLoadFrom) {
        try (
                final FileInputStream fileInputStream = new FileInputStream(fileLoadFrom);
                final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            final Object readObject = objectInputStream.readObject();
            return Optional.of((StorageSettings) readObject);
        } catch (final ClassNotFoundException | IOException exception) {
            return Optional.empty();
        }
    }
}
