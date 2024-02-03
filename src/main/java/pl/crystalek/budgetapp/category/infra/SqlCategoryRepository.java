package pl.crystalek.budgetapp.category.infra;

import com.zaxxer.hikari.HikariDataSource;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.category.Category;
import pl.crystalek.budgetapp.category.CategoryDTO;
import pl.crystalek.budgetapp.category.CategoryRepository;
import pl.crystalek.budgetapp.storage.function.SQLFunction;
import pl.crystalek.budgetapp.storage.util.SqlUtil;
import pl.crystalek.budgetapp.util.ColorUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class SqlCategoryRepository extends SqlUtil implements CategoryRepository {
    String loadCategoriesQuery;
    String createCategoryQuery;
    String loadCategoriesDtoQuery;
    String updateCategory;
    String categoriesTable;

    public SqlCategoryRepository(final HikariDataSource database, final String loadCategoriesQuery, final String loadCategoriesDtoQuery, final String createCategoryQuery, final String updateCategory, String categoriesTable) {
        super(database);
        this.loadCategoriesQuery = loadCategoriesQuery;
        this.loadCategoriesDtoQuery = loadCategoriesDtoQuery;
        this.createCategoryQuery = createCategoryQuery;
        this.updateCategory = updateCategory;
        this.categoriesTable = categoriesTable;

        createTable();
    }

    @Override
    public Set<Category> loadCategories() {
        final SQLFunction<ResultSet, Set<Category>> loadCategoriesFunction = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new HashSet<>();
            }

            final Set<Category> categoryList = new HashSet<>();

            do {
                final int id = resultSet.getInt("id");
                final String categoryName = resultSet.getString("name");
                final String colorHex = resultSet.getString("color_hex");

                final Category category = new Category(id, categoryName, Color.web(colorHex));
                categoryList.add(category);
            } while (resultSet.next());

            return categoryList;
        };

        return executeQueryAndOpenConnection(loadCategoriesQuery, loadCategoriesFunction);
    }

    @Override
    public Set<CategoryDTO> loadCategoriesDTO() {
        final SQLFunction<ResultSet, Set<CategoryDTO>> loadCategoriesFunction = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new HashSet<>();
            }

            final Set<CategoryDTO> categoryList = new HashSet<>();

            do {
                final int id = resultSet.getInt("id");
                final String categoryName = resultSet.getString("name");

                final CategoryDTO categoryDTO = new CategoryDTO(categoryName, id);
                categoryList.add(categoryDTO);
            } while (resultSet.next());

            return categoryList;
        };

        return executeQueryAndOpenConnection(loadCategoriesDtoQuery, loadCategoriesFunction);
    }

    @Override
    public boolean createCategory(final String categoryName, final String hexColor) {
        try (final PreparedStatement statement = database.getConnection().prepareStatement(createCategoryQuery)) {
            completionStatement(statement, categoryName, hexColor);
            statement.executeUpdate();

            return true;
        } catch (final SQLException exception) {
            return false;
        }
    }

    @Override
    public boolean updateCategory(final Category category) {
        try (final PreparedStatement statement = database.getConnection().prepareStatement(updateCategory)) {
            completionStatement(statement, category.getCategoryName(), ColorUtil.getHexFromColor(category.getCategoryColor()), category.getCategoryId());
            statement.executeUpdate();

            return true;
        } catch (final SQLException exception) {
            return false;
        }
    }

    @Override
    public void createTable() {
        executeUpdateAndOpenConnection(categoriesTable);
    }
}
