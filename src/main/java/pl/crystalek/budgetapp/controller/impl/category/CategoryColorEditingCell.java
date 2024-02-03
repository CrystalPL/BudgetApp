package pl.crystalek.budgetapp.controller.impl.category;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import pl.crystalek.budgetapp.category.Category;

class CategoryColorEditingCell extends TableCell<Category, Color> {

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);

        final ColorPicker colorPicker = new ColorPicker(getItem());
        colorPicker.setStyle("-fx-background-color: white");
        colorPicker.setMinWidth(getWidth() - getGraphicTextGap() * 2);
        colorPicker.setOnAction((event) -> commitEdit(colorPicker.getValue()));

        setGraphic(colorPicker);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setGraphic(new Circle(8, getItem()));
    }

    @Override
    public void updateItem(Color item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(new Circle(8, item));
        }

    }
}
