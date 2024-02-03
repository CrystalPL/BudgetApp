package pl.crystalek.budgetapp.controller.impl.receipt.edit;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptService;

import java.util.Comparator;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductNameSuggestion implements Comparable<String> {
    final ReceiptService receiptService;
    final ResourceRepository resourceRepository;
    final TextField aroundTextField;

    @Getter
    ComboBox<String> combobox;

    private void addToTextField() {
        aroundTextField.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                combobox.hide();
                return;
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                return;
            }

            if (event.getCode() != KeyCode.UP && event.getCode() != KeyCode.DOWN) {
                combobox.getSelectionModel().clearSelection();
            }

            aroundTextField.requestFocus();
            combobox.getItems().sort(Comparator.comparing(this::compareTo));
            if (!combobox.isShowing()) {
                combobox.show();
            }
        });
    }

    private void createComboBox(final Set<String> names) {
        addToTextField();

        combobox = new ComboBox<>();
        combobox.getItems().setAll(names);
        combobox.getStylesheets().add(resourceRepository.getStyleSheet("shopNameEdit"));

        final ComboBoxListViewSkin<String> skin = new ComboBoxListViewSkin<>(combobox);
        skin.getPopupContent().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            aroundTextField.setText(combobox.getValue());
            combobox.hide();
        });
        skin.getPopupContent().addEventHandler(KeyEvent.ANY, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
            }

            final String selectedItem = combobox.getSelectionModel().getSelectedItem();
            if (event.getCode() == KeyCode.ENTER && selectedItem != null) {
                aroundTextField.setText(combobox.getValue());
                combobox.hide();
            }
        });

        combobox.setSkin(skin);

        combobox.setLayoutX(aroundTextField.localToSceneTransformProperty().get().getTx());
        combobox.setLayoutY(aroundTextField.localToSceneTransformProperty().get().getTy() + 30);

        ((BorderPane) aroundTextField.getScene().getRoot()).getChildren().add(combobox);

        combobox.show();
        aroundTextField.requestFocus();
    }

    public void removeComboBox() {
        if (combobox != null) {
            ((BorderPane) combobox.getScene().getRoot()).getChildren().remove(combobox);
        }
    }

    public void createSuggestions(final NameType nameType) {
        switch (nameType) {
            case SHOP -> receiptService.fetchShopNames(this::createComboBox);
            case PRODUCT -> receiptService.fetchProductNames(this::createComboBox);
        }

    }

    @Override
    public int compareTo(final String o) {
        final String shopName = o.toLowerCase().replace(" ", "");
        final String textFieldName = aroundTextField.getText().toLowerCase().replace(" ", "");

        return shopName.startsWith(textFieldName) ? 0 : 1;
    }

    public enum NameType {
        SHOP, PRODUCT
    }
}
