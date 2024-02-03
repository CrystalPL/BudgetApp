package pl.crystalek.budgetapp.controller.impl.category;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.category.CategoryService;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.util.WindowUtil;
import pl.crystalek.budgetapp.view.ViewManager;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryController implements Controller {

    @Injectable
    CategoryService categoryService;

    @Injectable
    ViewManager viewManager;

    @FXML
    Button cancelButton;

    @FXML
    ColorPicker categoryColorPicker;

    @FXML
    TextField categoryNameTextField;

    @FXML
    Button createButton;

    @FXML
    Label dialogLabel;

    @FXML
    private void cancel(final ActionEvent event) {
        viewManager.openWindow(CategoryController.class);
    }

    @FXML
    private void create(final ActionEvent event) {
        dialogLabel.setTextFill(Color.web("#e03838"));

        final String labelText = dialogLabel.getText();
        if (!labelText.isBlank() && !labelText.isEmpty()) {
            categoryNameTextField.requestFocus();
            clear();
            return;
        }

        final String textFieldText = categoryNameTextField.getText();
        if (textFieldText.isBlank() || textFieldText.isEmpty()) {
            dialogLabel.setText("Musisz wpisać nazwę kategorii!");
            return;
        }

        if (textFieldText.length() > 64) {
            dialogLabel.setText("Nazwa kategorii może zawierać maksymalnie 64 znaki!");
            return;
        }

        if (categoryColorPicker.getValue().equals(Color.BLACK)) {
            dialogLabel.setText("Musisz wybrać kolor kategorii!");
            return;
        }

        categoryService.createCategory(textFieldText.trim(), categoryColorPicker.getValue(), value -> {
            if (!value) {
                dialogLabel.setText("Kategoria o takiej nazwie lub kolorze już istnieje!");
                return;
            }

            dialogLabel.setTextFill(Color.web("#36e158"));
            dialogLabel.setText("Pomyślnie utworzono kategorie.");
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
        WindowUtil.focusGainedListener(categoryNameTextField, () -> dialogLabel.setText(""));
        WindowUtil.focusGainedListener(categoryColorPicker, () -> dialogLabel.setText(""));

        WindowUtil.enterPressListener(categoryNameTextField, () -> {
            categoryColorPicker.requestFocus();
            categoryColorPicker.show();
        });

        WindowUtil.enterPressListener(categoryColorPicker, () -> createButton.requestFocus());
    }

    private void clear() {
        dialogLabel.setText("");
        categoryNameTextField.setText("");
        categoryColorPicker.setValue(Color.BLACK);
    }
}