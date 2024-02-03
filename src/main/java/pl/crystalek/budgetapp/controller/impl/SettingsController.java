package pl.crystalek.budgetapp.controller.impl;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.io.load.SettingsLoader;
import pl.crystalek.budgetapp.io.save.SettingsSaver;
import pl.crystalek.budgetapp.storage.StorageSettings;
import pl.crystalek.budgetapp.storage.StorageType;
import pl.crystalek.budgetapp.view.ViewManager;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettingsController implements Controller {

    @Injectable
    ResourceRepository resourceRepository;

    @Injectable
    ViewManager viewManager;

    @FXML
    TextField databaseHostnameTextField;

    @FXML
    TextField databaseNameTextField;

    @FXML
    TextField databasePasswordTextField;

    @FXML
    TextField databasePortTextField;

    @FXML
    TextField databaseUserTextField;

    @FXML
    VBox databaseVBox;

    @FXML
    CheckBox localeDataCheckBox;

    @FXML
    private void exportAppData(final ActionEvent event) {
        final File directory = directoryChooser();
    }

    @FXML
    private void exportSettings(final ActionEvent event) {
        final File directory = directoryChooser();

        SettingsSaver.saveSettings(resourceRepository.getStorageSettings(), new File(directory, "settings.budget"));
    }

    @FXML
    private void importAppData(final ActionEvent event) {
        final File file = fileChooser();


    }

    @FXML
    private void importSettings(final ActionEvent event) {
        final File file = fileChooser();

        final Optional<StorageSettings> settingsOptional = SettingsLoader.loadSettings(file);
        if (settingsOptional.isEmpty()) {
            viewManager.createDialog("Błąd ładowania", "Nie udało się załadować ustawień", DialogController.InfoType.ERROR, getClass());
            return;
        }

        final StorageSettings storageSettings = settingsOptional.get();
        resourceRepository.setStorageSettings(storageSettings);
        SettingsSaver.saveSettings(storageSettings, ResourceRepository.CREDENTIALS_FILE);
    }

    @Override
    public void openWindowListener() {
        final StorageSettings storageSettings = resourceRepository.getStorageSettings();
        databaseHostnameTextField.setText(storageSettings.getDatabaseHostname());
        databasePasswordTextField.setText(storageSettings.getDatabasePassword());
        databasePortTextField.setText(storageSettings.getDatabasePort());
        databaseUserTextField.setText(storageSettings.getDatabaseUsername());
        databaseNameTextField.setText(storageSettings.getDatabase());
    }

    @Override
    public void closeWindowListener() {
        final StorageSettings storageSettings = resourceRepository.getStorageSettings();
        storageSettings.setDatabaseHostname(databaseHostnameTextField.getText());
        storageSettings.setDatabasePassword(databasePasswordTextField.getText());
        storageSettings.setDatabasePort(databasePortTextField.getText());
        storageSettings.setDatabaseUsername(databaseUserTextField.getText());
        storageSettings.setDatabase(databaseNameTextField.getText());
        storageSettings.setStorageType(localeDataCheckBox.isSelected() ? StorageType.LOCALE : StorageType.MYSQL);

        SettingsSaver.saveSettings(storageSettings, ResourceRepository.CREDENTIALS_FILE);
    }

    @Override
    public void init() {

    }

    private File fileChooser() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik do zapisu");
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Budget Files", "*.budget"));

        final Window window = Stage.getWindows().stream().filter(Window::isShowing).findFirst().get();

        return fileChooser.showSaveDialog(window);
    }

    private File directoryChooser() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wybierz folder do zapisu pliku");
        directoryChooser.setInitialDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());

        final Window window = Stage.getWindows().stream().filter(Window::isShowing).findFirst().get();

        return directoryChooser.showDialog(window);
    }
}