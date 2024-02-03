package pl.crystalek.budgetapp.controller.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import pl.crystalek.budgetapp.controller.impl.bill.BillsController;
import pl.crystalek.budgetapp.controller.impl.category.CategoryController;
import pl.crystalek.budgetapp.controller.impl.receipt.main.ReceiptController;
import pl.crystalek.budgetapp.controller.impl.user.UsersController;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.view.ViewManager;

public class MainController {

    @Injectable
    private ViewManager viewManager;

    @FXML
    private BorderPane borderPane;

    @FXML
    private void analysis(final ActionEvent event) {
        viewManager.openWindow(AnalysisController.class);
    }

    @FXML
    private void bills(final ActionEvent event) {
        viewManager.openWindow(BillsController.class);
    }

    @FXML
    private void category(final ActionEvent event) {
        viewManager.openWindow(CategoryController.class);
    }

    @FXML
    private void receipt(final ActionEvent event) {
        viewManager.openWindow(ReceiptController.class);
    }

    @FXML
    private void storageSettings(final ActionEvent event) {
        viewManager.openWindow(SettingsController.class);
    }

    @FXML
    private void users(final ActionEvent event) {
        viewManager.openWindow(UsersController.class);
    }

    @FXML
    private void statistic(final ActionEvent event) {
        viewManager.openWindow(StatisticController.class);
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }
}
