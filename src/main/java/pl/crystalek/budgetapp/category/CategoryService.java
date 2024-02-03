package pl.crystalek.budgetapp.category;

import javafx.scene.paint.Color;
import pl.crystalek.budgetapp.util.ColorUtil;
import pl.crystalek.budgetapp.util.ThreadUtil;

import java.util.Set;
import java.util.function.Consumer;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void fetchCategories(final Consumer<Set<Category>> consumer) {
        ThreadUtil.runAsync(() -> ThreadUtil.runInGUIThread(() -> consumer.accept(categoryRepository.loadCategories())));
    }

    public void fetchCategoriesDTO(final Consumer<Set<CategoryDTO>> consumer) {
        ThreadUtil.runAsync(() -> ThreadUtil.runInGUIThread(() -> consumer.accept(categoryRepository.loadCategoriesDTO())));
    }

    public void updateCategory(final Category category, final Consumer<Boolean> consumer) {
        ThreadUtil.runAsync(() -> {
            final boolean updateCategory = categoryRepository.updateCategory(category);
            ThreadUtil.runInGUIThread(() -> consumer.accept(updateCategory));
        });
    }

    public void createCategory(final String categoryName, final Color color, final Consumer<Boolean> consumer) {
        final String hexFromColor = ColorUtil.getHexFromColor(color);
        ThreadUtil.runAsync(() -> {
            final boolean createCategory = categoryRepository.createCategory(categoryName, hexFromColor);
            ThreadUtil.runInGUIThread(() -> consumer.accept(createCategory));
        });
    }
}
