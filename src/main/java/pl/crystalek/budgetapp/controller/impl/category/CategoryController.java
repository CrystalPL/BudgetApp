package pl.crystalek.budgetapp.controller.impl.category;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.category.Category;
import pl.crystalek.budgetapp.category.CategoryService;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.controller.impl.DialogController;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.view.ViewManager;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryController implements Controller {

    @Injectable
    ViewManager viewManager;

    @Injectable
    CategoryService categoryService;

    @Injectable
    ResourceRepository resourceRepository;

    @FXML
    TableView<Category> tableView;

    @FXML
    TableColumn<Category, String> categoryColumn;

    @FXML
    TableColumn<Category, Color> colorColumn;

    @FXML
    private void createCategory(final ActionEvent event) {
        viewManager.openWindow(CreateCategoryController.class);
    }

    @FXML
    private void editColor(final TableColumn.CellEditEvent<Category, Color> event) {
        final Color newColor = event.getNewValue();
        final Category editedCategory = event.getRowValue();
        editedCategory.setCategoryColor(newColor);

        categoryService.updateCategory(editedCategory, update -> {
            if (update) {
                return;
            }

            viewManager.createDialog("Błąd", "Kategoria z takim kolorem już istnieje!", DialogController.InfoType.ERROR, getClass());
        });
    }

    @FXML
    private void editName(final TableColumn.CellEditEvent<Category, String> event) {
        final String newCategoryName = event.getNewValue().trim();
        if (newCategoryName.isEmpty() || newCategoryName.isBlank()) {
            viewManager.createDialog("Błąd", "Nie podano nazwy kategorii!", DialogController.InfoType.ERROR, getClass());
            return;
        }

        if (newCategoryName.length() > 64) {
            viewManager.createDialog("Błąd", "Nazwa kategorii może zawierać maksymalnie 64 znaki!", DialogController.InfoType.ERROR, getClass());
            return;
        }

        final Category editedCategory = event.getRowValue();
        editedCategory.setCategoryName(newCategoryName);
        categoryService.updateCategory(editedCategory, update -> {
            if (update) {
                return;
            }

            viewManager.createDialog("Błąd", "Kategoria z taką nazwą już istnieje!", DialogController.InfoType.ERROR, getClass());
        });
    }

    @Override
    public void openWindowListener() {
        categoryService.fetchCategories(categories -> {
            tableView.getItems().addAll(categories);
            tableView.refresh();
        });
    }

    @Override
    public void closeWindowListener() {
        tableView.getItems().clear();
    }

    @Override
    public void init() {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("categoryColor"));

        categoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        colorColumn.setCellFactory(param -> new CategoryColorEditingCell());

        tableView.setPlaceholder(new Label("Trwa ładownie danych..."));
    }
}
