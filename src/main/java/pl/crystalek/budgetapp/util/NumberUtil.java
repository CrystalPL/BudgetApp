package pl.crystalek.budgetapp.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@UtilityClass
public class NumberUtil {
    public final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00#", new DecimalFormatSymbols(new Locale("pl", "PL")));

    public String formatNumber(final Object number) {
        return DECIMAL_FORMAT.format(number).replace(",00", "");
    }

    public Optional<Number> parseString(final Object object) {
        if (!(object instanceof String)) {
            return Optional.empty();
        }

        final String stringNumber = ((String) object).replace(" ", "");
        try {
            return Optional.of(DECIMAL_FORMAT.parse(stringNumber));
        } catch (final ParseException exception) {
            return Optional.empty();
        }
    }

    public Optional<Long> getLong(final Object number) {
        return parseString(number).map(Number::longValue);
    }

    public Optional<Short> getShort(final Object number) {
        return parseString(number).map(Number::shortValue);
    }

    public Optional<Integer> getInt(final Object number) {
        return parseString(number).map(Number::intValue);
    }

    public Optional<Double> getDouble(final Object number) {
        return parseString(number).map(Number::doubleValue);
    }

    public Optional<Float> getFloat(final Object number) {
        return parseString(number).map(Number::floatValue);
    }
}