package pl.crystalek.budgetapp.io.save;

import lombok.experimental.UtilityClass;
import pl.crystalek.budgetapp.storage.StorageSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@UtilityClass
public class SettingsSaver {

    public void saveSettings(final StorageSettings storageSettings, final File fileToSave) {
        try (
                final FileOutputStream fileOutputStream = new FileOutputStream(fileToSave);
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(storageSettings);
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
