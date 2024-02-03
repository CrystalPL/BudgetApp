package pl.crystalek.budgetapp.main;

import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.crystalek.budgetapp.DataAnalyzer;
import pl.crystalek.budgetapp.category.CategoryRepository;
import pl.crystalek.budgetapp.category.CategoryRepositoryFactory;
import pl.crystalek.budgetapp.category.CategoryService;
import pl.crystalek.budgetapp.controller.Controller;
import pl.crystalek.budgetapp.controller.ControllerData;
import pl.crystalek.budgetapp.controller.impl.MainController;
import pl.crystalek.budgetapp.di.DependencyInjector;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptRepository;
import pl.crystalek.budgetapp.receipt.ReceiptRepositoryFactory;
import pl.crystalek.budgetapp.receipt.ReceiptService;
import pl.crystalek.budgetapp.storage.Storage;
import pl.crystalek.budgetapp.storage.StorageService;
import pl.crystalek.budgetapp.storage.StorageSettings;
import pl.crystalek.budgetapp.storage.StorageType;
import pl.crystalek.budgetapp.user.UserRepository;
import pl.crystalek.budgetapp.user.UserRepositoryFactory;
import pl.crystalek.budgetapp.user.UserService;
import pl.crystalek.budgetapp.view.ViewManager;

import java.io.IOException;

public class BudgetApp extends Application {
    private StorageService storageService;

    @Override
    public void start(Stage stage) throws IOException {
        final ResourceRepository resourceRepository = new ResourceRepository();
        resourceRepository.checkFilesExist();
        resourceRepository.loadResources();

        final StorageSettings storageSettings = resourceRepository.getStorageSettings();
        storageService = new StorageService(storageSettings).create();
        final Storage storage = storageService.getStorage();
        storage.connect();
        storage.testConnection();

        final HikariDataSource database = storage.getDataSource();
        final StorageType storageType = storageSettings.getStorageType();

        final CategoryRepository categoryRepository = CategoryRepositoryFactory.createCategoryRepository(database, storageType);
        final CategoryService categoryService = new CategoryService(categoryRepository);

        final UserRepository userRepository = UserRepositoryFactory.createUserRepository(database, storageType);
        final UserService userService = new UserService(userRepository);

        final ReceiptRepository receiptRepository = ReceiptRepositoryFactory.createReceiptRepository(database, storageType);
        final ReceiptService receiptService = new ReceiptService(receiptRepository);

        final ViewManager viewManager = new ViewManager(resourceRepository);

        DependencyInjector.registerDependency(resourceRepository);
        DependencyInjector.registerDependency(categoryService);
        DependencyInjector.registerDependency(userService);
        DependencyInjector.registerDependency(receiptService);
        DependencyInjector.registerDependency(viewManager);
        DependencyInjector.injectDependencies(resourceRepository.getControllerDataMap());
        resourceRepository.getControllerDataMap().values().stream()
                .map(ControllerData::classController)
                .filter(clazz -> Controller.class.isAssignableFrom(clazz.getClass()))
                .map(clazz -> (Controller) clazz)
                .forEach(Controller::init);

        final ControllerData<MainController> controllerData = resourceRepository.getControllerData(MainController.class);
        final Scene scene = new Scene(controllerData.pane());
        scene.getStylesheets().add(resourceRepository.getStyleSheet("contextMenu"));
        scene.getRoot().requestFocus();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        final DataAnalyzer dataAnalyzer = new DataAnalyzer(database);
        dataAnalyzer.printAnalysis();
    }

    @Override
    public void stop() throws Exception {
        if (storageService == null) {
            return;
        }

        storageService.getStorage().close();
    }
}