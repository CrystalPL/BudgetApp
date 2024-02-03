package pl.crystalek.budgetapp.controller.impl.receipt.main.edit;

import javafx.util.StringConverter;

public class BooleanConverter extends StringConverter<Boolean> {

    @Override
    public String toString(final Boolean object) {
        return object ? "Tak" : "Nie";
    }

    @Override
    public Boolean fromString(final String string) {
        return string.equals("Tak");
    }
}