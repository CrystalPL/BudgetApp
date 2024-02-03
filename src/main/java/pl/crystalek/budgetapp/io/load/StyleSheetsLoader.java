package pl.crystalek.budgetapp.io.load;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class StyleSheetsLoader {

    private StyleSheetsLoader() {
    }

    public static Map<String, String> loadStyleSheets() throws IOException {
        final String[] cssNames = {"shopNameEdit", "datepicker", "textfield", "combobox", "contextMenu"};

        final Map<String, String> styleSheetMap = new HashMap<>();
        for (final String styleSheetName : cssNames) {
            final String resourcePath = "/css/" + styleSheetName + ".css";

            final URL resource = StyleSheetsLoader.class.getResource(resourcePath);
            if (resource == null) {
                throw new IOException("Error while loading css file: " + styleSheetName);
            }

            final String externalForm = resource.toExternalForm();
            styleSheetMap.put(styleSheetName, externalForm);
        }

        return styleSheetMap;
    }
}
