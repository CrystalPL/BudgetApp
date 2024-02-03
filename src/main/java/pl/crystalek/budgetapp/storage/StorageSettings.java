package pl.crystalek.budgetapp.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.io.ResourceRepository;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class StorageSettings implements Serializable {
    @Serial private static final long serialVersionUID = -4426413444618246974L;
    String databaseHostname;
    String databasePort;
    String databaseUsername;
    String databasePassword;
    String database;
    File localeStorageLocation = ResourceRepository.PROGRAM_DIRECTORY;
    StorageType storageType = StorageType.LOCALE;
}
