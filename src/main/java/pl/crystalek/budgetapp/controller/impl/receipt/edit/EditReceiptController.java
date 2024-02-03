package pl.crystalek.budgetapp.controller.impl.receipt.edit;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.category.CategoryDTO;
import pl.crystalek.budgetapp.category.CategoryService;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.controller.impl.DialogController;
import pl.crystalek.budgetapp.controller.impl.receipt.CategoryConverter;
import pl.crystalek.budgetapp.controller.impl.receipt.NameSuggestions;
import pl.crystalek.budgetapp.controller.impl.receipt.edit.edit.CategoryEditingCell;
import pl.crystalek.budgetapp.controller.impl.receipt.edit.edit.DoubleConverter;
import pl.crystalek.budgetapp.controller.impl.receipt.main.ReceiptController;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptItem;
import pl.crystalek.budgetapp.receipt.ReceiptService;
import pl.crystalek.budgetapp.util.ColorUtil;
import pl.crystalek.budgetapp.util.NumberUtil;
import pl.crystalek.budgetapp.util.WindowUtil;
import pl.crystalek.budgetapp.view.ViewManager;

import java.util.List;
import java.util.Optional;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EditReceiptController implements Controller {

    @Setter
    int currentEditedReceiptId;

    @Injectable
    CategoryService categoryService;

    @Injectable
    ReceiptService receiptService;

    @Injectable
    ViewManager viewManager;

    @Injectable
    ResourceRepository resourceRepository;

    ProductNameSuggestion productNameSuggestion;

    @FXML
    TextField amountTextField;

    @FXML
    Button addButton;

    @FXML
    ComboBox<CategoryDTO> categoryComboBox;

    @FXML
    Label dialogLabel;

    @FXML
    TextField productNameTextField;

    @FXML
    ComboBox<Double> divisionOfMoneyComboBox;

    @FXML
    TextField priceTextField;

    @FXML
    TableView<ReceiptItem> tableView;

    @FXML
    TableColumn<ReceiptItem, Double> amountColumn;

    @FXML
    TableColumn<ReceiptItem, CategoryDTO> categoryColumn;

    @FXML
    TableColumn<ReceiptItem, String> productColumn;

    @FXML
    TableColumn<ReceiptItem, Double> priceColumn;

    @FXML
    TableColumn<ReceiptItem, Double> divisionOfMoneyColumn;

    @FXML
    private void addProduct(final ActionEvent event) {
        dialogLabel.setTextFill(Color.web("#e03838"));

        final String labelText = dialogLabel.getText();
        if (!labelText.isBlank() && !labelText.isEmpty()) {
            productNameTextField.requestFocus();
            clear();
            return;
        }

        final String productName = productNameTextField.getText();
        if (productName.isBlank() || productName.isEmpty()) {
            dialogLabel.setText("Musisz wpisać nazwę!");
            return;
        }

        final Optional<Double> amountOptional = NumberUtil.getDouble(amountTextField.getText());
        if (amountOptional.isEmpty()) {
            dialogLabel.setText("Musisz wpisać ilość!");
            return;
        }

        final Optional<Double> priceOptional = NumberUtil.getDouble(priceTextField.getText());
        if (priceOptional.isEmpty()) {
            dialogLabel.setText("Musisz wpisać cenę!");
            return;
        }

        final CategoryDTO category = categoryComboBox.getValue();
        if (category == null) {
            dialogLabel.setText("Wybierz kategorie");
            return;
        }

        final Double divisionMoney = divisionOfMoneyComboBox.getValue();
        if (divisionMoney == null) {
            dialogLabel.setText("Wybierz dzielenie");
            return;
        }

        final ReceiptItem receiptItem = new ReceiptItem(productName, amountOptional.get(), priceOptional.get(), category, divisionMoney);
        tableView.getItems().add(receiptItem);

        dialogLabel.setTextFill(Color.web("#36e158"));
        dialogLabel.setText("Pomyślnie dodano przedmiot.");
    }

    @FXML
    private void editAmount(final TableColumn.CellEditEvent<ReceiptItem, Double> event) {
        final Double newValue = event.getNewValue();
        if (newValue == null) {
            dialogLabel.setTextFill(ColorUtil.ERROR_COLOR);
            dialogLabel.setText("Ilość musi być liczbą!");
            return;
        }

        final ReceiptItem receiptItem = event.getRowValue();
        if (receiptItem.getAmount() == newValue) {
            return;
        }

        receiptItem.setAmount(newValue);
        receiptService.updateReceiptItem(receiptItem);
    }

    @FXML
    private void editCategory(final TableColumn.CellEditEvent<ReceiptItem, CategoryDTO> event) {
        final CategoryDTO newCategory = event.getNewValue();
        final ReceiptItem receiptItem = event.getRowValue();
        if (receiptItem.getCategory().equals(newCategory)) {
            return;
        }

        receiptItem.setCategory(newCategory);
        receiptService.updateReceiptItem(receiptItem);
    }

    @FXML
    private void editMoneyDivision(final TableColumn.CellEditEvent<ReceiptItem, Double> event) {
        final Double newValue = event.getNewValue();
        final ReceiptItem receiptItem = event.getRowValue();
        if (receiptItem.getMoneyDivision() == newValue) {
            return;
        }

        receiptItem.setMoneyDivision(newValue);
        receiptService.updateReceiptItem(receiptItem);
    }

    @FXML
    private void editPrice(final TableColumn.CellEditEvent<ReceiptItem, Double> event) {
        final Double newValue = event.getNewValue();
        if (newValue == null) {
            dialogLabel.setText("Wartość musi być liczbą");
            return;
        }

        final ReceiptItem receiptItem = event.getRowValue();
        if (receiptItem.getPrice() == newValue) {
            return;
        }

        receiptItem.setPrice(newValue);
        receiptService.updateReceiptItem(receiptItem);
    }

    @FXML
    private void editProduct(final TableColumn.CellEditEvent<ReceiptItem, String> event) {
        final String newValue = event.getNewValue();
        if (newValue.isEmpty() || newValue.isBlank()) {
            viewManager.createDialog("Błąd", "Nie podano nazwy", DialogController.InfoType.ERROR, getClass());
            return;
        }

        final ReceiptItem receiptItem = event.getRowValue();
        if (receiptItem.getProductName().equals(newValue)) {
            return;
        }

        receiptItem.setProductName(newValue);
        receiptService.updateReceiptItem(receiptItem);
    }

    @FXML
    private void finish(final ActionEvent event) {
        viewManager.openWindow(ReceiptController.class);
    }

    @FXML
    private void reset(final ActionEvent event) {
        clear();
    }

    @FXML
    private void deleteReceiptItem(final ActionEvent event) {
        final ReceiptItem selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        tableView.getItems().remove(selectedItem);
        if (selectedItem.getItemId() != 0) {
            receiptService.deleteReceiptItem(selectedItem);
        }
    }

    @Override
    public void openWindowListener() {
        receiptService.fetchReceiptItems(currentEditedReceiptId, items -> tableView.getItems().setAll(items));
        categoryService.fetchCategoriesDTO(categories -> categoryComboBox.getItems().setAll(categories));
        productNameSuggestion = new ProductNameSuggestion(receiptService, resourceRepository, productNameTextField);
        productNameSuggestion.createSuggestions(ProductNameSuggestion.NameType.PRODUCT);
    }

    @Override
    public void closeWindowListener() {
        final ObservableList<ReceiptItem> items = tableView.getItems();

        final List<ReceiptItem> itemToInsert = items.stream()
                .filter(item -> item.getItemId() == 0)
                .toList();

        if (!itemToInsert.isEmpty()) {
            receiptService.addItem(itemToInsert, currentEditedReceiptId);
        }

        items.clear();
        categoryComboBox.getItems().clear();
        clear();
        currentEditedReceiptId = 0;
        productNameSuggestion.removeComboBox();
        productNameSuggestion = null;
    }

    @Override
    public void init() {
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        divisionOfMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("moneyDivision"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        divisionOfMoneyComboBox.getItems().setAll(0.25, 0.33, 0.66, 1.0, 0.75, 0.5, 0.0);
        divisionOfMoneyComboBox.setConverter(new DoubleConverter());

        WindowUtil.enterPressListener(productNameTextField, () -> amountTextField.requestFocus());
        WindowUtil.enterPressListener(amountTextField, () -> priceTextField.requestFocus());
        WindowUtil.enterPressListener(priceTextField, () -> categoryComboBox.requestFocus());
        WindowUtil.enterPressListener(categoryComboBox, () -> divisionOfMoneyComboBox.requestFocus());
        WindowUtil.enterPressListener(divisionOfMoneyComboBox, () -> addButton.requestFocus());

        WindowUtil.focusGainedListener(productNameTextField, () -> dialogLabel.setText(""));
        WindowUtil.focusGainedListener(amountTextField, () -> dialogLabel.setText(""));
        WindowUtil.focusGainedListener(priceTextField, () -> dialogLabel.setText(""));

        WindowUtil.focusGainedListener(categoryComboBox, () -> {
            dialogLabel.setText("");
            categoryComboBox.show();
        });

        WindowUtil.focusGainedListener(divisionOfMoneyComboBox, () -> {
            dialogLabel.setText("");
            divisionOfMoneyComboBox.show();
        });

        categoryComboBox.setConverter(new CategoryConverter(categoryComboBox));

        productColumn.setCellFactory(cell -> new NameSuggestions<>(resourceRepository, receiptService, NameSuggestions.NameType.PRODUCT));
        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleConverter()));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleConverter()));
        divisionOfMoneyColumn.setCellFactory(cell -> {
            final ComboBoxTableCell<ReceiptItem, Double> divisionMoneyEditing = new ComboBoxTableCell<>(new DoubleConverter(), divisionOfMoneyComboBox.getItems());
            divisionMoneyEditing.getStylesheets().add(resourceRepository.getStyleSheet("combobox"));

            return divisionMoneyEditing;
        });

        categoryColumn.setCellFactory(cell -> new CategoryEditingCell(resourceRepository, categoryComboBox));

        tableView.setPlaceholder(new Label("Trwa ładownie danych..."));
    }

    private void clear() {
        amountTextField.setText("");
        priceTextField.setText("");
        productNameTextField.setText("");
        dialogLabel.setText("");

        categoryComboBox.getSelectionModel().clearSelection();
        divisionOfMoneyComboBox.getSelectionModel().clearSelection();
    }
}
