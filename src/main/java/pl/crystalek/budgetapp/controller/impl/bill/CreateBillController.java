package pl.crystalek.budgetapp.controller.impl.bill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import pl.crystalek.budgetapp.controller.Controller;

public class CreateBillController implements Controller {

    @FXML
    private TextField amountTextField;

    @FXML
    private ComboBox<?> categoryComboBox;

    @FXML
    private Label dialogLabel;

    @FXML
    private ComboBox<?> monthComboBox;

    @FXML
    private ComboBox<?> yearComboBox;

    @FXML
    void cancelButton(ActionEvent event) {

    }

    @FXML
    void createButton(ActionEvent event) {

    }

    @FXML
    void textFieldPressed(KeyEvent event) {

    }

    @Override
    public void openWindowListener() {

    }

    @Override
    public void closeWindowListener() {

    }

    @Override
    public void init() {

    }
}
