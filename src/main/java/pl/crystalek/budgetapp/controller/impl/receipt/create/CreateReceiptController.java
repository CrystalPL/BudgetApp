package pl.crystalek.budgetapp.controller.impl.receipt.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.controller.impl.receipt.main.ReceiptController;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptService;
import pl.crystalek.budgetapp.user.UserDTO;
import pl.crystalek.budgetapp.user.UserService;
import pl.crystalek.budgetapp.util.ColorUtil;
import pl.crystalek.budgetapp.util.WindowUtil;
import pl.crystalek.budgetapp.view.ViewManager;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateReceiptController implements Controller {

    int lastCreateReceiptId;

    @Injectable
    ResourceRepository resourceRepository;

    @Injectable
    ViewManager viewManager;

    @Injectable
    ReceiptService receiptService;

    @Injectable
    UserService userService;

    @FXML
    Button createButton;

    @FXML
    Button addItemsButton;

    @FXML
    Label dialogLabel;

    @FXML
    CheckBox receiptSettledCheckBox;

    @FXML
    TextField shopNameTextField;

    @FXML
    DatePicker shoppingDatePicker;

    @FXML
    ComboBox<UserDTO> whoPaidComboBox;

    @FXML
    private void addItems(final ActionEvent event) {
        if (lastCreateReceiptId == 0) {
            dialogLabel.setTextFill(ColorUtil.ERROR_COLOR);
            dialogLabel.setText("Najpierw musisz utworzyć paragon!");
            return;
        }

        viewManager.showReceiptItems(lastCreateReceiptId);
    }

    @FXML
    private void cancel(final ActionEvent event) {
        viewManager.openWindow(ReceiptController.class);
    }

    @FXML
    private void create(final ActionEvent event) {
        dialogLabel.setTextFill(ColorUtil.ERROR_COLOR);

        final String labelText = dialogLabel.getText();
        if (!labelText.isBlank() && !labelText.isEmpty()) {
            addItemsButton.requestFocus();
            clear();
            return;
        }

        if (shoppingDatePicker.getValue() == null) {
            dialogLabel.setText("Musisz wpisać date zakupów!");
            return;
        }

        if (whoPaidComboBox.getValue() == null) {
            dialogLabel.setText("Musisz wybrać kto zapłacił!");
            return;
        }

        final String shopName = shopNameTextField.getText();
        if (shopName.isEmpty() || shopName.isBlank()) {
            dialogLabel.setText("Musisz wpisać nazwę sklepu!");
            return;
        }

        if (shopName.length() > 64) {
            dialogLabel.setText("Nazwa sklepu może zawierać maksymalnie 64 znaki!");
            return;
        }

        receiptService.createReceipt(shopName, shoppingDatePicker.getValue(), whoPaidComboBox.getValue(), receiptSettledCheckBox.isSelected(), receiptId -> lastCreateReceiptId = receiptId);

        dialogLabel.setTextFill(ColorUtil.SUCCESS_COLOR);
        dialogLabel.setText("Pomyślnie stworzono paragon.");
    }

    @Override
    public void openWindowListener() {
        userService.fetchUsersDTO(list -> whoPaidComboBox.getItems().setAll(list));
    }

    @Override
    public void closeWindowListener() {
        clear();
    }

    @Override
    public void init() {
        shoppingDatePicker.setShowWeekNumbers(false);

        WindowUtil.focusGainedListener(shoppingDatePicker, () -> {
            dialogLabel.setText("");
            shoppingDatePicker.show();
        });
        WindowUtil.focusGainedListener(whoPaidComboBox, () -> {
            dialogLabel.setText("");
            whoPaidComboBox.show();
        });
        WindowUtil.focusGainedListener(shopNameTextField, () -> dialogLabel.setText(""));

        WindowUtil.enterPressListener(shoppingDatePicker, () -> whoPaidComboBox.requestFocus());
        WindowUtil.enterPressListener(whoPaidComboBox, () -> shopNameTextField.requestFocus());
        WindowUtil.enterPressListener(shopNameTextField, () -> createButton.requestFocus());

        whoPaidComboBox.setConverter(new PayerConverter(whoPaidComboBox));
    }

    private void clear() {
        shoppingDatePicker.setValue(null);
        shopNameTextField.setText("");
        receiptSettledCheckBox.setSelected(false);
        dialogLabel.setText("");
        whoPaidComboBox.getItems().clear();
        lastCreateReceiptId = 0;
    }
}
