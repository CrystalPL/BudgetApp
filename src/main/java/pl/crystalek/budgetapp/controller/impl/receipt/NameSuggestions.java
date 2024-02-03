package pl.crystalek.budgetapp.controller.impl.receipt;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptService;

import java.util.Comparator;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NameSuggestions<T> extends TableCell<T, String> implements Comparable<String> {
    final ResourceRepository resourceRepository;
    final ReceiptService receiptService;
    final NameType nameType;

    TextField textField;
    ComboBox<String> combobox;

    @Override
    public void startEdit() {
        super.startEdit();

        createTextField();
        createSuggestions();
        textField.requestFocus();
    }

    @Override
    protected void updateItem(final String item, final boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText("");
            return;
        }

        setText(item);
        setGraphic(null);
    }

    private void createTextField() {
        textField = new TextField(getItem());
        textField.getStylesheets().add(resourceRepository.getStyleSheet("textfield"));

        setGraphic(textField);

        textField.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
                ((BorderPane) combobox.getScene().getRoot()).getChildren().remove(combobox);
                return;
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                return;
            }

            if (event.getCode() != KeyCode.UP && event.getCode() != KeyCode.DOWN) {
                combobox.getSelectionModel().clearSelection();
            }

            textField.requestFocus();
            combobox.getItems().sort(Comparator.comparing(this::compareTo));
            if (!combobox.isShowing()) {
                combobox.show();
            }
        });
    }

    private void fetchAndSetNames(final Set<String> names) {
        combobox = new ComboBox<>();
        combobox.getItems().setAll(names);
        combobox.getStylesheets().add(resourceRepository.getStyleSheet("shopNameEdit"));

        final ComboBoxListViewSkin<String> skin = new ComboBoxListViewSkin<>(combobox);
        skin.getPopupContent().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            commitEdit(combobox.getValue());
            ((BorderPane) combobox.getScene().getRoot()).getChildren().remove(combobox);
        });
        skin.getPopupContent().addEventHandler(KeyEvent.ANY, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
            }

            final String selectedItem = combobox.getSelectionModel().getSelectedItem();
            if (event.getCode() == KeyCode.ENTER && selectedItem != null) {
                commitEdit(selectedItem);
                ((BorderPane) combobox.getScene().getRoot()).getChildren().remove(combobox);
            }
        });

        combobox.setSkin(skin);

        combobox.setLayoutX(textField.localToSceneTransformProperty().get().getTx());
        combobox.setLayoutY(textField.localToSceneTransformProperty().get().getTy() + 30);

        ((BorderPane) textField.getScene().getRoot()).getChildren().add(combobox);

        combobox.show();
        textField.requestFocus();
    }

    private void createSuggestions() {
        switch (nameType) {
            case SHOP -> receiptService.fetchShopNames(this::fetchAndSetNames);
            case PRODUCT -> receiptService.fetchProductNames(this::fetchAndSetNames);
        }

    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
        setText(getItem());

        ((BorderPane) combobox.getScene().getRoot()).getChildren().remove(combobox);
    }

    @Override
    public int compareTo(final String o) {
        final String shopName = o.toLowerCase().replace(" ", "");
        final String textFieldName = textField.getText().toLowerCase().replace(" ", "");

        return shopName.startsWith(textFieldName) ? 0 : 1;
    }

    public enum NameType {
        SHOP, PRODUCT
    }

}
