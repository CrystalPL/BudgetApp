package pl.crystalek.budgetapp.util;

import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadUtil {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

    private ThreadUtil() {
    }

    public static void runAsync(final Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    public static void runInGUIThread(final Runnable runnable) {
        Platform.runLater(runnable);
    }
}
