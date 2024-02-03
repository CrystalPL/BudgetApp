package pl.crystalek.budgetapp.controller.impl.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.user.UserService;
import pl.crystalek.budgetapp.user.sex.SexConverter;
import pl.crystalek.budgetapp.user.sex.UserSex;
import pl.crystalek.budgetapp.util.FileChooserUtil;
import pl.crystalek.budgetapp.util.WindowUtil;
import pl.crystalek.budgetapp.view.ViewManager;

import java.io.File;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserController implements Controller {

    private File chosenFile;

    @Injectable
    ViewManager viewManager;

    @Injectable
    UserService userService;

    @FXML
    TextField userNameTextField;

    @FXML
    ComboBox<UserSex> sexComboBox;

    @FXML
    Button iconButton;

    @FXML
    Button createButton;

    @FXML
    Label dialogLabel;

    @FXML
    private void cancel(final ActionEvent event) {
        viewManager.openWindow(UsersController.class);
    }

    @FXML
    private void chooseIcon(final ActionEvent event) {
        final Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setAlwaysOnTop(false);
        chosenFile = FileChooserUtil.createFileChooser();
        stage.setAlwaysOnTop(true);
        createButton.requestFocus();
    }

    @FXML
    private void create(final ActionEvent event) {
        dialogLabel.setTextFill(Color.web("#e03838"));

        final String labelText = dialogLabel.getText();
        if (!labelText.isBlank() && !labelText.isEmpty()) {
            userNameTextField.requestFocus();
            clear();
            return;
        }

        final String userName = userNameTextField.getText();
        if (userName.isEmpty() || userName.isBlank()) {
            dialogLabel.setText("Nie podano nazwy użytkownika!");
            return;
        }

        final UserSex sex = sexComboBox.getValue();
        if (sex == null) {
            dialogLabel.setText("Nie wybrano płci!");
            return;
        }

        if (chosenFile == null || chosenFile.isDirectory()) {
            dialogLabel.setText("Plik nie został poprawnie wybrany!");
            return;
        }

        userService.createUser(userName, sex, chosenFile, isUserCreate -> {
            if (!isUserCreate) {
                dialogLabel.setText("Użytkownik o takiej nazwie lub ikonie już istnieje!");
                return;
            }

            dialogLabel.setTextFill(Color.web("#36e158"));
            dialogLabel.setText("Pomyślnie utworzono użytkownika.");
        });
    }

    @Override
    public void openWindowListener() {

    }

    @Override
    public void closeWindowListener() {
        clear();
    }

    @Override
    public void init() {
        WindowUtil.focusGainedListener(userNameTextField, () -> dialogLabel.setText(""));
        WindowUtil.focusGainedListener(sexComboBox, () -> dialogLabel.setText(""));
        WindowUtil.focusGainedListener(iconButton, () -> dialogLabel.setText(""));

        WindowUtil.enterPressListener(userNameTextField, () -> {
            sexComboBox.requestFocus();
            sexComboBox.show();
        });

        WindowUtil.enterPressListener(sexComboBox, () -> iconButton.requestFocus());

        sexComboBox.getItems().addAll(UserSex.values());
//        sexComboBox.setCellFactory(cell -> new UserSexCell());
        sexComboBox.setConverter(SexConverter.INSTANCE);
    }

    private void clear() {
        userNameTextField.setText("");
        dialogLabel.setText("");
        sexComboBox.setValue(null);
        chosenFile = null;
    }

}
