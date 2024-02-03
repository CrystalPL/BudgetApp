package pl.crystalek.budgetapp.category;

import java.util.Set;

public interface CategoryRepository {

    Set<Category> loadCategories();

    Set<CategoryDTO> loadCategoriesDTO();

    boolean createCategory(final String categoryName, final String hexColor);

    boolean updateCategory(final Category category);

    void createTable();
}