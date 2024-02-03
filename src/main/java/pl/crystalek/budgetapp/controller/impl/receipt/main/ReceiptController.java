package pl.crystalek.budgetapp.controller.impl.receipt.main;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.controller.impl.DialogController;
import pl.crystalek.budgetapp.controller.impl.receipt.NameSuggestions;
import pl.crystalek.budgetapp.controller.impl.receipt.create.CreateReceiptController;
import pl.crystalek.budgetapp.controller.impl.receipt.main.edit.BooleanConverter;
import pl.crystalek.budgetapp.controller.impl.receipt.main.edit.DateEditingCell;
import pl.crystalek.budgetapp.controller.impl.receipt.main.edit.PurchaseAmountEditingCell;
import pl.crystalek.budgetapp.controller.impl.receipt.main.edit.UserEditingCell;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptDTO;
import pl.crystalek.budgetapp.receipt.ReceiptService;
import pl.crystalek.budgetapp.user.UserDTO;
import pl.crystalek.budgetapp.user.UserService;
import pl.crystalek.budgetapp.view.ViewManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceiptController implements Controller {

    List<ReceiptDTO> unmodifiableReceiptList;

    @Injectable
    ViewManager viewManager;

    @Injectable
    ReceiptService receiptService;

    @Injectable
    UserService userService;

    @Injectable
    ResourceRepository resourceRepository;

    @FXML
    DatePicker toDatePicker;

    @FXML
    DatePicker fromDatePicker;

    @FXML
    TextField searchTextField;

    @FXML
    TableView<ReceiptDTO> tableView;

    @FXML
    TableColumn<ReceiptDTO, Double> amountForPurchaseColumn;

    @FXML
    TableColumn<ReceiptDTO, String> shopNameColumn;

    @FXML
    TableColumn<ReceiptDTO, LocalDate> shoppingDateColumn;

    @FXML
    TableColumn<ReceiptDTO, UserDTO> whoPaidColumn;

    @FXML
    TableColumn<ReceiptDTO, Boolean> settledColumn;

    @FXML
    private void addReceipt(final ActionEvent event) {
        viewManager.openWindow(CreateReceiptController.class);
    }

    @FXML
    private void editSettled(final TableColumn.CellEditEvent<ReceiptDTO, Boolean> event) {
        final Boolean newValue = event.getNewValue();
        final ReceiptDTO receiptDTO = event.getRowValue();
        if (receiptDTO.isSettled() == newValue) {
            return;
        }

        receiptDTO.setSettled(newValue);
        receiptService.updateReceipt(receiptDTO);
    }

    @FXML
    private void editShopName(final TableColumn.CellEditEvent<ReceiptDTO, String> event) {
        final String newValue = event.getNewValue();
        if (newValue.isEmpty() || newValue.isBlank()) {
            viewManager.createDialog("Błąd", "Nie podano nazwy", DialogController.InfoType.ERROR, getClass());
            return;
        }

        if (newValue.length() > 64) {
            viewManager.createDialog("Błąd", "Nazwa sklepu może zawierać maksymalnie 64 znaki!", DialogController.InfoType.ERROR, getClass());
            return;
        }

        final ReceiptDTO receiptDTO = event.getRowValue();
        if (receiptDTO.getShopName().equals(newValue)) {
            return;
        }

        receiptDTO.setShopName(newValue);
        receiptService.updateReceipt(receiptDTO);
    }

    @FXML
    private void editShoppingDate(final TableColumn.CellEditEvent<ReceiptDTO, LocalDate> event) {
        final LocalDate newValue = event.getNewValue();
        if (newValue == null) {
            viewManager.createDialog("Błąd", "Nie wybrano daty zakupów", DialogController.InfoType.ERROR, getClass());
            return;
        }

        final ReceiptDTO receiptDTO = event.getRowValue();
        if (receiptDTO.getShoppingDate().isEqual(newValue)) {
            return;
        }

        receiptDTO.setShoppingDate(newValue);
        receiptService.updateReceipt(receiptDTO);
    }

    @FXML
    private void editWhoPaid(final TableColumn.CellEditEvent<ReceiptDTO, UserDTO> event) {
        final UserDTO newValue = event.getNewValue();
        if (newValue == null) {
            viewManager.createDialog("Błąd", "Nie wybrano kto zapłacił", DialogController.InfoType.ERROR, getClass());
            return;
        }

        final ReceiptDTO receiptDTO = event.getRowValue();
        if (receiptDTO.getUserDTO().equals(newValue)) {
            return;
        }

        receiptDTO.getUserDTO().setPayerId(newValue.getPayerId());
        receiptService.updateReceipt(receiptDTO);
    }

    @FXML
    private void searchByShopName(final KeyEvent event) {
        final String searchText = searchTextField.getText();
        if (searchText.isEmpty() || searchText.isBlank()) {
            tableView.getItems().setAll(unmodifiableReceiptList);
            return;
        }

        final Predicate<ReceiptDTO> searchPredicate = receiptDTO -> {
            final String shopName = receiptDTO.getShopName().toLowerCase().replace(" ", "");
            final String search = searchText.toLowerCase().replace(" ", "");

            return shopName.startsWith(search);
        };

        final List<ReceiptDTO> sortedList = unmodifiableReceiptList.stream()
                .filter(searchPredicate)
                .sorted(Comparator.comparing(searchPredicate::test))
                .toList();

        tableView.getItems().setAll(sortedList);
    }

    @FXML
    private void deleteReceipt(final ActionEvent event) {
        final ReceiptDTO selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        final Runnable okButtonRunnable = () -> {
            receiptService.deleteReceipt(selectedItem);
            tableView.getItems().remove(selectedItem);
            unmodifiableReceiptList.remove(selectedItem);
        };

        final String context = "Kliknij poniższy przycisk, jeśli chcesz ten paragon i pozostałe dodane do niego przedmioty,";
        viewManager.createDialog("Usunięcie paragonu", context, DialogController.InfoType.INFO, null, okButtonRunnable);
    }

    @FXML
    private void openReceiptItems(final ActionEvent event) {
        final ReceiptDTO selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        viewManager.showReceiptItems(selectedItem.getReceiptId());
    }

    @Override
    public void openWindowListener() {
        receiptService.fetchReceipts(list -> {
            tableView.getItems().setAll(list);
            unmodifiableReceiptList = new ArrayList<>(list);
        });
    }

    @Override
    public void closeWindowListener() {
        tableView.getItems().clear();
        unmodifiableReceiptList = null;
        searchTextField.setText("");
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
    }

    @Override
    public void init() {
        amountForPurchaseColumn.setCellValueFactory(new PropertyValueFactory<>("amountForPurchase"));
        shopNameColumn.setCellValueFactory(new PropertyValueFactory<>("shopName"));
        shoppingDateColumn.setCellValueFactory(new PropertyValueFactory<>("shoppingDate"));
        whoPaidColumn.setCellValueFactory(new PropertyValueFactory<>("userDTO"));
        settledColumn.setCellValueFactory(new PropertyValueFactory<>("settled"));

        shopNameColumn.setCellFactory(cell -> new NameSuggestions<>(resourceRepository, receiptService, NameSuggestions.NameType.SHOP));
        shoppingDateColumn.setCellFactory(cell -> new DateEditingCell(resourceRepository));
        whoPaidColumn.setCellFactory(cell -> new UserEditingCell(userService, resourceRepository));
        settledColumn.setCellFactory(cell -> {
            final ComboBoxTableCell<ReceiptDTO, Boolean> settleEditingCell = new ComboBoxTableCell<>(new BooleanConverter(), Boolean.TRUE, Boolean.FALSE);
            settleEditingCell.getStylesheets().add(resourceRepository.getStyleSheet("combobox"));

            return settleEditingCell;
        });

        amountForPurchaseColumn.setCellFactory(cell -> new PurchaseAmountEditingCell<>());

        fromDatePicker.setShowWeekNumbers(false);
        fromDatePicker.setOnAction(event -> searchByDate(fromDatePicker.getValue(), toDatePicker.getValue()));

        toDatePicker.setShowWeekNumbers(false);
        toDatePicker.setOnAction(event -> searchByDate(fromDatePicker.getValue(), toDatePicker.getValue()));

        tableView.setPlaceholder(new Label("Trwa ładownie danych..."));
    }

    private void searchByDate(final LocalDate from, final LocalDate to) {
        if (from == null || to == null) {
            tableView.getItems().setAll(unmodifiableReceiptList);
            tableView.getItems().setAll(unmodifiableReceiptList);
            return;
        }

        final List<ReceiptDTO> list = unmodifiableReceiptList.stream()
                .filter(receiptDTO -> receiptDTO.getShoppingDate().isAfter(from) && receiptDTO.getShoppingDate().isBefore(to))
                .toList();

        tableView.getItems().setAll(list);
    }
}