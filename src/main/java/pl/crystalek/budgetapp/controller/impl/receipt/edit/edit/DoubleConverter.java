package pl.crystalek.budgetapp.controller.impl.receipt.edit.edit;

import javafx.util.StringConverter;
import pl.crystalek.budgetapp.util.NumberUtil;

public class DoubleConverter extends StringConverter<Double> {

    @Override
    public String toString(final Double object) {
        return NumberUtil.formatNumber(object);
    }

    @Override
    public Double fromString(final String string) {
        return NumberUtil.getDouble(string).orElse(null);
    }
}
