package pl.crystalek.budgetapp.controller.impl.receipt.create;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.user.UserDTO;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class PayerConverter extends StringConverter<UserDTO> {
    ComboBox<UserDTO> comboBox;

    @Override
    public String toString(final UserDTO object) {
        return object.getPayerName();
    }

    @Override
    public UserDTO fromString(final String string) {
        return comboBox.getItems().stream()
                .filter(categoryDTO -> categoryDTO.getPayerName().equals(string))
                .findFirst()
                .orElse(null);
    }
}
