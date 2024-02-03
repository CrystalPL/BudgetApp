package pl.crystalek.budgetapp.controller.impl.receipt;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.category.CategoryDTO;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryConverter extends StringConverter<CategoryDTO> {
    ComboBox<CategoryDTO> comboBox;

    @Override
    public String toString(final CategoryDTO object) {
        return object.getCategoryName();
    }

    @Override
    public CategoryDTO fromString(final String string) {
        return comboBox.getItems().stream()
                .filter(categoryDTO -> categoryDTO.getCategoryName().equals(string))
                .findFirst()
                .orElse(null);
    }
}