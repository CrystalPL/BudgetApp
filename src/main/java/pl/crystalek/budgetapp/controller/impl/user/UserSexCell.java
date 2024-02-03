package pl.crystalek.budgetapp.controller.impl.user;

import javafx.scene.control.ListCell;
import pl.crystalek.budgetapp.user.sex.UserSex;

public class UserSexCell extends ListCell<UserSex> {

    @Override
    protected void updateItem(final UserSex item, final boolean empty) {
        super.updateItem(item, empty);

        setText(item.getPolishTranslation());
    }
}
