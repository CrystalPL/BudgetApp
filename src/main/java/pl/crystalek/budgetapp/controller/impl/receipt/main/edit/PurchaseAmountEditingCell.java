package pl.crystalek.budgetapp.controller.impl.receipt.main.edit;

import javafx.scene.control.TableCell;
import pl.crystalek.budgetapp.util.NumberUtil;

public class PurchaseAmountEditingCell<T> extends TableCell<T, Double> {

    @Override
    protected void updateItem(final Double item, final boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText("");
            return;
        }

        setText(NumberUtil.formatNumber(item));
    }
}
