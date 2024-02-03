package pl.crystalek.budgetapp.util;

import javafx.scene.paint.Color;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ColorUtil {

    public final Color ERROR_COLOR = Color.web("#e03838");
    public final Color SUCCESS_COLOR = Color.web("#36e158");

    public String getHexFromColor(final Color color) {
        if (color == null) {
            return "";
        }

        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
