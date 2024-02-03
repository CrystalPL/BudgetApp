package pl.crystalek.budgetapp.util;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.controller.ControllerData;

public final class WindowUtil {
    private static final Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();
    private static double X;
    private static double Y;

    private WindowUtil() {
    }

    public static void focusGainedListener(final Node nodeToFocus, final Runnable runnable) {
        nodeToFocus.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                return;
            }

            runnable.run();
        });
    }

    public static void enterPressListener(final Node nodeToHandle, final Runnable runnable) {
        nodeToHandle.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() != KeyCode.ENTER) {
                return;
            }

            runnable.run();
        });
    }

    public static Stage prepareWindow(final ControllerData<? extends Controller> controllerData) {
        final Parent paneToOpen = controllerData.pane();
        final Stage stage = new Stage(StageStyle.UNDECORATED);
        final Scene scene = new Scene(paneToOpen);
        scene.setUserData(controllerData.classController());

        paneToOpen.setOnMousePressed(event -> {
            X = event.getSceneX();
            Y = event.getSceneY();
        });

        paneToOpen.setOnMouseDragged(event -> {
            if (event.getScreenY() < SCREEN_BOUNDS.getMaxY() - 20) {
                stage.setY(event.getScreenY() - Y);
            }

            stage.setX(event.getScreenX() - X);
        });

        paneToOpen.setOnMouseReleased(event -> {
            if (stage.getY() < 0.0) {
                stage.setY(0.0);
            }
        });

        paneToOpen.requestFocus();

        stage.setAlwaysOnTop(true);
        stage.setScene(scene);

        return stage;
    }
}
