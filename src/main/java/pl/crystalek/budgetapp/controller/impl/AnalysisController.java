package pl.crystalek.budgetapp.controller.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import pl.crystalek.budgetapp.controller.Controller;

public class AnalysisController implements Controller {

    @FXML
    private ComboBox<?> chartTypeComboBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private Label monthMoneyLabel;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    void showPeriodBills(ActionEvent event) {

    }

    @FXML
    void showPeriodExpenses(ActionEvent event) {

    }

    @FXML
    void showYearBills(ActionEvent event) {

    }

    @FXML
    void showYearExpenses(ActionEvent event) {

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
