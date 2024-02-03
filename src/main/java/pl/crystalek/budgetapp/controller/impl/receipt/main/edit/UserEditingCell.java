package pl.crystalek.budgetapp.controller.impl.receipt.main.edit;

import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.StringConverter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptDTO;
import pl.crystalek.budgetapp.user.UserDTO;
import pl.crystalek.budgetapp.user.UserService;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserEditingCell extends ComboBoxTableCell<ReceiptDTO, UserDTO> {
    UserService userService;

    public UserEditingCell(final UserService userService, final ResourceRepository resourceRepository) {
        super();

        this.userService = userService;

        getStylesheets().add(resourceRepository.getStyleSheet("combobox"));
        setConverter(new UserConverter());
    }

    @Override
    public void startEdit() {
        super.startEdit();
        userService.fetchUsersDTO(users -> getItems().setAll(users));
    }

    @Override
    public void updateItem(final UserDTO item, final boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.getPayerName());
        }
    }

    private class UserConverter extends StringConverter<UserDTO> {

        @Override
        public String toString(final UserDTO object) {
            return object.getPayerName();
        }

        @Override
        public UserDTO fromString(final String string) {
            return getItems().stream()
                    .filter(userDTO -> userDTO.getPayerName().equals(string))
                    .findFirst()
                    .orElse(null);
        }
    }
}
