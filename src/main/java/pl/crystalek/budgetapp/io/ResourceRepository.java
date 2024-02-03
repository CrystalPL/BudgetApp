package pl.crystalek.budgetapp.io;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import pl.crystalek.budgetapp.controller.ControllerData;
import pl.crystalek.budgetapp.io.load.ImageLoader;
import pl.crystalek.budgetapp.io.load.PaneLoader;
import pl.crystalek.budgetapp.io.load.SettingsLoader;
import pl.crystalek.budgetapp.io.load.StyleSheetsLoader;
import pl.crystalek.budgetapp.storage.StorageSettings;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ResourceRepository {
    public static final File PROGRAM_DIRECTORY = new File(FileSystemView.getFileSystemView().getDefaultDirectory(), "BudgetApp");
    public static final File CREDENTIALS_FILE = new File(ResourceRepository.PROGRAM_DIRECTORY, "settings.budget");

    private Map<String, Image> imageMap;
    @Getter
    private Map<Class<?>, ControllerData<?>> controllerDataMap;
    private Map<String, String> styleSheetMap;
    @Getter
    @Setter
    private StorageSettings storageSettings;

    public void checkFilesExist() throws IOException {
        if (!PROGRAM_DIRECTORY.exists()) {
            PROGRAM_DIRECTORY.mkdir();
        }

        if (!CREDENTIALS_FILE.exists()) {
            CREDENTIALS_FILE.createNewFile();
        }
    }

    public void loadResources() throws IOException {
        this.imageMap = ImageLoader.loadImages();
        this.controllerDataMap = PaneLoader.loadControllerData();
        this.styleSheetMap = StyleSheetsLoader.loadStyleSheets();
        this.storageSettings = SettingsLoader.loadSettings(CREDENTIALS_FILE).orElse(new StorageSettings());
    }

    public Image getImage(final String imageName) {
        return imageMap.get(imageName);
    }

    public <T> ControllerData<T> getControllerData(final Class<T> controllerClass) {
        return (ControllerData<T>) controllerDataMap.get(controllerClass);
    }

    public String getStyleSheet(final String styleSheetName) {
        return styleSheetMap.get(styleSheetName);
    }
}
