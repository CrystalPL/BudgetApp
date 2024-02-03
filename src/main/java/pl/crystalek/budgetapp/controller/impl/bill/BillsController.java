package pl.crystalek.budgetapp.controller.impl.bill;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.crystalek.budgetapp.controller.Controller;

public class BillsController implements Controller {

    @FXML
    private TableColumn<?, ?> amountForPurchaseColumn;

    @FXML
    private TableColumn<?, ?> categoryColumn;

    @FXML
    private TableColumn<?, ?> consumptionColumn;

    @FXML
    private TableColumn<?, ?> periodColumn;

    @FXML
    private TableView<?> tableView;

    @FXML
    void addConsumption(ActionEvent event) {

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
