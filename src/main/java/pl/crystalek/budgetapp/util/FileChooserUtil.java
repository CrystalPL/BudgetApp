package pl.crystalek.budgetapp.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public final class FileChooserUtil {
    private FileChooserUtil() {
    }

    public static File createFileChooser() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        fileChooser.setTitle("Wybierz zdjęcie");

        final Window window = Stage.getWindows().stream().filter(Window::isShowing).findFirst().get();

        return fileChooser.showOpenDialog(window);
    }

    public static ImageView createImageView(final Image image) {
        final ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        return imageView;
    }
}
