package pl.crystalek.budgetapp.controller.impl.user;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.crystalek.budgetapp.user.User;
import pl.crystalek.budgetapp.util.FileChooserUtil;

import java.io.File;

public class FileChooseTableCell extends TableCell<User, ImageView> {

    @Override
    public void startEdit() {
        super.startEdit();

        final File chosenFile = FileChooserUtil.createFileChooser();
        if (chosenFile == null) {
            commitEdit(null);
            return;
        }

        final Image image = new Image(chosenFile.toURI().toString());
        commitEdit(FileChooserUtil.createImageView(image));
    }

    @Override
    protected void updateItem(final ImageView item, final boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            setGraphic(item);
        }
    }
}
