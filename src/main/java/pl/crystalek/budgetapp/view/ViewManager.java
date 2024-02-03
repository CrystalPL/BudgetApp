package pl.crystalek.budgetapp.view;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.controller.ControllerData;
import pl.crystalek.budgetapp.controller.impl.DialogController;
import pl.crystalek.budgetapp.controller.impl.MainController;
import pl.crystalek.budgetapp.controller.impl.category.CreateCategoryController;
import pl.crystalek.budgetapp.controller.impl.receipt.create.CreateReceiptController;
import pl.crystalek.budgetapp.controller.impl.receipt.edit.EditReceiptController;
import pl.crystalek.budgetapp.controller.impl.user.CreateUserController;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.util.WindowUtil;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewManager {
    final Map<Class<? extends Controller>, Stage> stageMap;
    final ResourceRepository resourceRepository;

    ControllerData<? extends Controller> currentOpenWindow;
    Stage currentOpenStage;

    public ViewManager(final ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
        this.stageMap = buildStages();
    }

    private Map<Class<? extends Controller>, Stage> buildStages() {
        final Class<? extends Controller>[] classes = new Class[]{CreateCategoryController.class, CreateUserController.class,
                CreateReceiptController.class, DialogController.class}; //, EditReceiptController.class

        final Map<Class<? extends Controller>, Stage> stageMap = new HashMap<>();
        for (final Class<? extends Controller> clazz : classes) {
            final ControllerData<? extends Controller> controllerData = resourceRepository.getControllerData(clazz);
            final Stage stage = WindowUtil.prepareWindow(controllerData);
            stage.getScene().getStylesheets().add(resourceRepository.getStyleSheet("contextMenu"));
            stageMap.put(clazz, stage);
        }

        return stageMap;
    }

    public void openWindow(final Class<? extends Controller> controllerClass) {
        final ControllerData<? extends Controller> controllerData = resourceRepository.getControllerData(controllerClass);

        if (stageMap.containsKey(controllerClass)) {
            final Stage stage = stageMap.get(controllerClass);
            stage.getScene().getRoot().getChildrenUnmodifiable().forEach(Node::requestFocus);
            stage.requestFocus();
            stage.show();

            currentOpenStage = stage;
        } else {
            final BorderPane borderPane = resourceRepository.getControllerData(MainController.class).classController().getBorderPane();
            borderPane.setCenter(controllerData.pane());
            borderPane.requestFocus();

            closeWindow();
            currentOpenWindow = controllerData;
        }

        controllerData.classController().openWindowListener();
    }

    public void closeWindow() {
        closeCurrentStage();

        if (currentOpenWindow != null) {
            currentOpenWindow.classController().closeWindowListener();
        }
    }

    public void closeCurrentStage() {
        if (currentOpenStage != null) {
            ((Controller) currentOpenStage.getScene().getUserData()).closeWindowListener();
            currentOpenStage.close();
            currentOpenStage = null;
        }
    }

    public void createDialog(final String header, final String context, final DialogController.InfoType infoType, final Class<? extends Controller> classWhereDialogWasOpened) {
        createDialog(header, context, infoType, classWhereDialogWasOpened, null);
    }

    public void createDialog(final String header, final String context, final DialogController.InfoType infoType, final Class<? extends Controller> classWhereDialogWasOpened, final Runnable okButtonRunnable) {
        final DialogController dialogController = resourceRepository.getControllerData(DialogController.class).classController();
        dialogController.createDialog(header, context, infoType, classWhereDialogWasOpened, okButtonRunnable);

        openWindow(DialogController.class);
    }

    public void showReceiptItems(final int receiptId) {
        final EditReceiptController editReceiptController = resourceRepository.getControllerData(EditReceiptController.class).classController();
        editReceiptController.setCurrentEditedReceiptId(receiptId);

        openWindow(EditReceiptController.class);
    }
}
