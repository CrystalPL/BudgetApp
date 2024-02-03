package pl.crystalek.budgetapp.io.load;

import javafx.fxml.FXMLLoader;
import pl.crystalek.budgetapp.controller.ControllerData;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class PaneLoader {

    private PaneLoader() {
    }

    public static Map<Class<?>, ControllerData<?>> loadControllerData() throws IOException {
        final String[] paneNames = {
                "bill/billsPane", "bill/createBillPane", "category/categoryPane", "category/createCategory", "receipt/createReceiptPane",
                "receipt/editReceiptPane", "receipt/receiptPane", "analysisPane", "mainPane", "settingsPane", "statisticPane", "dialogPane",
                "user/createUserPane", "user/usersPane"
        };

        final Map<Class<?>, ControllerData<?>> paneMap = new HashMap<>();
        for (final String paneName : paneNames) {
            final String fxmlPath = "/fxml/" + paneName + ".fxml";
            try (
                    final InputStream inputStream = PaneLoader.class.getResourceAsStream(fxmlPath)
            ) {
                final FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.load(inputStream);

                final ControllerData<Object> controllerData = new ControllerData<>(fxmlLoader.getController(), fxmlLoader.getRoot());
                paneMap.put(fxmlLoader.getController().getClass(), controllerData);
            } catch (final Exception exception) {
                System.err.println("Błąd podczas wczytywania pliku FXML: " + fxmlPath);
                throw exception;
            }
        }

        return paneMap;
    }
}
