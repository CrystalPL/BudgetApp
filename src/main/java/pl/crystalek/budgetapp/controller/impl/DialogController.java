package pl.crystalek.budgetapp.controller.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.view.ViewManager;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DialogController implements Controller {

    Class<? extends Controller> classWhereDialogWasOpened;
    Runnable okButtonRunnable;

    @Injectable
    ViewManager viewManager;

    @Injectable
    ResourceRepository resourceRepository;

    @FXML
    Label contentTextLabel;

    @FXML
    Label headerTextLabel;

    @FXML
    ImageView infoImageView;

    @FXML
    private void okButton(final ActionEvent event) {
        if (okButtonRunnable != null) {
            okButtonRunnable.run();
        }

        if (classWhereDialogWasOpened != null) {
            viewManager.openWindow(classWhereDialogWasOpened);
        } else {
            viewManager.closeCurrentStage();
        }
    }

    @Override
    public void openWindowListener() {

    }

    @Override
    public void closeWindowListener() {
        contentTextLabel.setText("");
        headerTextLabel.setText("");
        infoImageView.setImage(null);
        this.classWhereDialogWasOpened = null;
        this.okButtonRunnable = null;
    }

    @Override
    public void init() {
        //empty :/
    }

    public void createDialog(final String header, final String context, final InfoType infoType, final Class<? extends Controller> classWhereDialogWasOpened) {
        createDialog(header, context, infoType, classWhereDialogWasOpened, null);
    }

    public void createDialog(final String header, final String context, final InfoType infoType, final Class<? extends Controller> classWhereDialogWasOpened, final Runnable okButtonRunnable) {
        headerTextLabel.setText(header);
        headerTextLabel.setTextFill(infoType.getTextColor());
        contentTextLabel.setText(context);
        infoImageView.setImage(resourceRepository.getImage(infoType.getImageName()));

        this.classWhereDialogWasOpened = classWhereDialogWasOpened;
        this.okButtonRunnable = okButtonRunnable;
    }

    public enum InfoType {
        INFO("dialog/info", Color.web("#00e5ff")),
        WARNING("dialog/warning", Color.web("#ff8400")),
        ERROR("dialog/error", Color.web("#ff0000"));

        private final String imageName;
        private final Color textColor;

        InfoType(final String imageName, final Color textColor) {
            this.imageName = imageName;
            this.textColor = textColor;
        }

        public String getImageName() {
            return imageName;
        }

        public Color getTextColor() {
            return textColor;
        }
    }
}
