package pl.crystalek.budgetapp.controller.impl.receipt.edit.edit;

import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.ComboBoxTableCell;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.category.CategoryDTO;
import pl.crystalek.budgetapp.controller.impl.receipt.CategoryConverter;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptItem;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryEditingCell extends ComboBoxTableCell<ReceiptItem, CategoryDTO> {
    ResourceRepository resourceRepository;
    ComboBox<CategoryDTO> comboBox;

    public CategoryEditingCell(final ResourceRepository resourceRepository, final ComboBox<CategoryDTO> comboBox) {
        this.resourceRepository = resourceRepository;
        this.comboBox = comboBox;

        setConverter(new CategoryConverter(comboBox));
        getStylesheets().add(resourceRepository.getStyleSheet("combobox"));
    }

    @Override
    public void startEdit() {
        super.startEdit();

        getItems().setAll(comboBox.getItems());
    }

    @Override
    public void updateItem(final CategoryDTO item, final boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            return;
        }

        setText(item.getCategoryName());
    }

}
