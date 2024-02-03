package pl.crystalek.budgetapp.io.load;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class ImageLoader {

    private ImageLoader() {
    }

    public static Map<String, Image> loadImages() throws IOException {
        final String[] imageNames = {
                "statistic/billsIcon", "statistic/expensiveCategoryIcon", "statistic/expensiveThingIcon", "statistic/gasConsumptionIcon", "statistic/powerCosumptionIcon",
                "statistic/purchasedTicketsIcon", "statistic/purchaseNumberIcon", "statistic/spentMoneyIcon", "statistic/visitedStoreIcon", "statistic/waterConsumptionIcon",
                "dialog/errorIcon", "dialog/infoIcon", "dialog/warningIcon"
        };

        final Map<String, Image> imageMap = new HashMap<>();
        for (final String imageName : imageNames) {
            final String imagePath = "/image/" + imageName + ".png";
            try (
                    final InputStream inputStream = ImageLoader.class.getResourceAsStream(imagePath)
            ) {
                final Image image = new Image(inputStream);

                imageMap.put(imageName.replace("Icon", ""), image);
            } catch (final Exception exception) {
                System.err.println("Błąd podczas wczytywania obrazka: " + imageName);
                throw exception;
            }
        }

        return imageMap;
    }
}
