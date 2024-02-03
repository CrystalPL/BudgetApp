package pl.crystalek.budgetapp.di;

import pl.crystalek.budgetapp.controller.ControllerData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class DependencyInjector {
    private static final Map<Class<?>, Object> DEPENDENCY_REGISTRY = new HashMap<>();

    private DependencyInjector() {
    }

    public static void registerDependency(final Object object) {
        DEPENDENCY_REGISTRY.put(object.getClass(), object);
    }

    private static void injectDependencies(final Object controllerObject) {
        for (final Field field : controllerObject.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Injectable.class)) {
                continue;
            }

            field.setAccessible(true);
            try {
                field.set(controllerObject, DEPENDENCY_REGISTRY.get(field.getType()));
            } catch (final IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public static void injectDependencies(final Map<Class<?>, ControllerData<?>> controllerDataMap) {
        controllerDataMap.values().stream()
                .map(ControllerData::classController)
                .forEach(DependencyInjector::injectDependencies);
    }
}
