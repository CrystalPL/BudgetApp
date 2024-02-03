package pl.crystalek.budgetapp.controller.impl.user;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.controller.impl.DialogController;
import pl.crystalek.budgetapp.di.Injectable;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.user.User;
import pl.crystalek.budgetapp.user.UserService;
import pl.crystalek.budgetapp.user.sex.UserSex;
import pl.crystalek.budgetapp.util.FileChooserUtil;
import pl.crystalek.budgetapp.view.ViewManager;

public class UsersController implements Controller {

    @Injectable
    private ViewManager viewManager;

    @Injectable
    private UserService userService;

    @Injectable
    private ResourceRepository resourceRepository;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, ImageView> iconColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> sexColumn;

    @FXML
    private void createUser(final ActionEvent event) {
        viewManager.openWindow(CreateUserController.class);
    }

    @FXML
    private void editIcon(final TableColumn.CellEditEvent<User, ImageView> event) {
        final ImageView imageView = event.getNewValue();
        if (imageView == null) {
            final DialogController dialogController = resourceRepository.getControllerData(DialogController.class).classController();
            dialogController.createDialog("Błąd", "Nie wybrano obrazka", DialogController.InfoType.ERROR, getClass());

            viewManager.openWindow(DialogController.class);
        }

//        final Image image = imageView.getImage();
//        final boolean imageAlreadyUsed = userRepository.getData().stream().anyMatch(user -> userRepository.isImageEqual(user.getIcon(), image));
//        if (imageAlreadyUsed) {
//            final DialogController dialogController = resourceRepository.getControllerData(DialogController.class).classController();
//            dialogController.createDialog("Błąd", "Obrazek jest już używany", DialogController.InfoType.ERROR);
//
//            viewManager.openWindow(DialogController.class);
//            return;
//        }

//        final User user = event.getRowValue();
//        user.setIcon(image);
//
//        userRepository.updateUser(user);
    }

    @FXML
    private void editName(final TableColumn.CellEditEvent<User, String> event) {
//        final String userName = event.getNewValue();
//        if (userName.isEmpty() || userName.isBlank()) {
//            final DialogController dialogController = resourceRepository.getControllerData(DialogController.class).classController();
//            dialogController.createDialog("Błąd", "Nie podano nazwy", DialogController.InfoType.ERROR);
//
//            viewManager.openWindow(DialogController.class);
//            return;
//        }
//
//        final boolean nameAlreadyUsed = userRepository.getData().stream().anyMatch(user -> user.getName().equalsIgnoreCase(userName));
//        if (nameAlreadyUsed) {
//            final DialogController dialogController = resourceRepository.getControllerData(DialogController.class).classController();
//            dialogController.createDialog("Błąd", "Nazwa jest już używana", DialogController.InfoType.ERROR);
//
//            viewManager.openWindow(DialogController.class);
//            return;
//        }

        final User user = event.getRowValue();
//        user.setName(userName);

//        userRepository.updateUser(user);
    }

    @FXML
    private void editSex(final TableColumn.CellEditEvent<User, String> event) {
        final String sexName = event.getNewValue();
        final UserSex sex = UserSex.getSexByPolishName(sexName);

        final User user = event.getRowValue();
        if (user.getSex() == sex) {
            return;
        }

        user.setSex(sex);

//        userRepository.updateUser(user);
    }

    @Override
    public void openWindowListener() {
        userService.fetchUsers(users -> tableView.getItems().setAll(users));
    }

    @Override
    public void closeWindowListener() {
        clear();
    }

    @Override
    public void init() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sexColumn.setCellValueFactory(user -> new SimpleObjectProperty<>(user.getValue().getSex().getPolishTranslation()));
        iconColumn.setCellValueFactory(user -> new SimpleObjectProperty<>(FileChooserUtil.createImageView(user.getValue().getIcon())));

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sexColumn.setCellFactory(ComboBoxTableCell.forTableColumn(UserSex.getPolishTranslations()));
        iconColumn.setCellFactory(factory -> new FileChooseTableCell());
    }

    private void clear() {
        tableView.getItems().clear();
    }
}
