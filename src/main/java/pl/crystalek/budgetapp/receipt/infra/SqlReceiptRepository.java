package pl.crystalek.budgetapp.receipt.infra;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.category.CategoryDTO;
import pl.crystalek.budgetapp.receipt.ReceiptDTO;
import pl.crystalek.budgetapp.receipt.ReceiptItem;
import pl.crystalek.budgetapp.receipt.ReceiptRepository;
import pl.crystalek.budgetapp.storage.function.SQLFunction;
import pl.crystalek.budgetapp.storage.util.SqlUtil;
import pl.crystalek.budgetapp.user.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class SqlReceiptRepository extends SqlUtil implements ReceiptRepository {
    String createReceiptQuery;
    String createItemQuery;
    String loadReceiptsQuery;
    String loadReceiptItemsQuery;
    String updateReceipt;
    String updateReceiptItem;
    String deleteByItemId;
    String deleteItemsByReceiptId;
    String deleteReceiptById;
    String getShopNameList;
    String getProductNameList;
    String receiptsTable;
    String itemsTable;

    public SqlReceiptRepository(final HikariDataSource database, final String createReceiptQuery, final String createItemQuery,
                                final String loadReceiptsQuery, final String loadReceiptItemsQuery, final String updateReceipt,
                                final String updateReceiptItem, final String deleteByItemId, final String deleteItemsByReceiptId,
                                final String deleteReceiptById, final String getShopNameList, final String getProductNameList,
                                final String receiptsTable, final String itemsTable) {
        super(database);

        this.createReceiptQuery = createReceiptQuery;
        this.createItemQuery = createItemQuery;
        this.loadReceiptsQuery = loadReceiptsQuery;
        this.loadReceiptItemsQuery = loadReceiptItemsQuery;
        this.updateReceipt = updateReceipt;
        this.updateReceiptItem = updateReceiptItem;
        this.deleteByItemId = deleteByItemId;
        this.deleteItemsByReceiptId = deleteItemsByReceiptId;
        this.deleteReceiptById = deleteReceiptById;
        this.getShopNameList = getShopNameList;
        this.getProductNameList = getProductNameList;
        this.receiptsTable = receiptsTable;
        this.itemsTable = itemsTable;

        createTable();
    }

    @Override
    public int createReceipt(final String shopName, final LocalDate shoppingDate, final UserDTO whoPaid, final boolean settled) {
        return openConnection(connection -> {
            executeUpdate(connection, createReceiptQuery, shopName, shoppingDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(), whoPaid.getPayerId(), settled);
            return getLastInsertId(connection);
        });
    }

    @Override
    public void addItem(final List<ReceiptItem> receiptItemList, final int receiptId) {
        openConnection(connection -> {
            @Cleanup final PreparedStatement statement = connection.prepareStatement(createItemQuery);

            connection.setAutoCommit(false);

            for (final ReceiptItem item : receiptItemList) {
                completionStatement(statement, item.getProductName(), item.getAmount(), item.getPrice(), item.getCategory().getCategoryId(), item.getMoneyDivision(), receiptId);
                statement.addBatch();
            }

            connection.commit();
            statement.executeBatch();
            connection.setAutoCommit(true);

        });
    }

    @Override
    public List<ReceiptDTO> getReceiptDTOList() {
        final SQLFunction<ResultSet, List<ReceiptDTO>> loadReceiptsFunction = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new ArrayList<>();
            }

            final List<ReceiptDTO> receiptDTOList = new ArrayList<>();
            do {
                final int receiptId = resultSet.getInt("receipt_id");
                final String shopName = resultSet.getString("shop_name");
                final LocalDate shoppingDate = Instant.ofEpochMilli(resultSet.getLong("shopping_date")).atZone(ZoneId.systemDefault()).toLocalDate();
                final boolean settled = resultSet.getBoolean("settled");
                final String payerName = resultSet.getString("user_name");
                final double amountForPurchase = resultSet.getDouble("amount_for_purchase");
                final int userId = resultSet.getInt("user_id");

                final ReceiptDTO receiptDTO = new ReceiptDTO(receiptId, amountForPurchase, payerName, shopName, shoppingDate, userId, settled);

                receiptDTOList.add(receiptDTO);
            } while (resultSet.next());

            return receiptDTOList;
        };

        return executeQueryAndOpenConnection(loadReceiptsQuery, loadReceiptsFunction);
    }

    @Override
    public List<ReceiptItem> getReceiptItems(final int receiptId) {
        final SQLFunction<ResultSet, List<ReceiptItem>> loadReceiptItemsFunction = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new ArrayList<>();
            }

            final List<ReceiptItem> receiptItemList = new ArrayList<>();
            do {
                final int itemId = resultSet.getInt("item_id");
                final String itemName = resultSet.getString("item_name");
                final double amount = resultSet.getDouble("amount");
                final double price = resultSet.getDouble("price");
                final double moneyDivision = resultSet.getDouble("money_division");

                final String categoryName = resultSet.getString("category_name");
                final int categoryId = resultSet.getInt("id");

                final CategoryDTO categoryDTO = new CategoryDTO(categoryName, categoryId);
                final ReceiptItem receiptItem = new ReceiptItem(itemName, amount, price, categoryDTO, moneyDivision, itemId);

                receiptItemList.add(receiptItem);
            } while (resultSet.next());

            return receiptItemList;
        };

        return executeQueryAndOpenConnection(loadReceiptItemsQuery, loadReceiptItemsFunction, receiptId);
    }

    @Override
    public void updateReceipt(final ReceiptDTO receipt) {
        executeUpdateAndOpenConnection(updateReceipt, receipt.getShopName(), receipt.getShoppingDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(), receipt.getUserDTO().getPayerId(), receipt.isSettled(), receipt.getReceiptId());
    }

    @Override
    public void updateReceiptItem(final ReceiptItem receiptItem) {
        executeUpdateAndOpenConnection(updateReceiptItem, receiptItem.getProductName(), receiptItem.getAmount(), receiptItem.getPrice(), receiptItem.getCategory().getCategoryId(), receiptItem.getMoneyDivision(), receiptItem.getItemId());
    }

    @Override
    public void deleteReceipt(final ReceiptDTO receipt) {
        openConnection(connection -> {
            final int receiptId = receipt.getReceiptId();

            executeUpdate(connection, deleteItemsByReceiptId, receiptId);
            executeUpdate(connection, deleteReceiptById, receiptId);
        });
    }

    @Override
    public void deleteReceiptItem(final ReceiptItem receiptItem) {
        executeUpdateAndOpenConnection(deleteByItemId, receiptItem.getItemId());
    }

    @Override
    public Set<String> getShopNames() {
        return getNames(getShopNameList);
    }

    @Override
    public Set<String> getProductNames() {
        return getNames(getProductNameList);
    }

    @Override
    public void createTable() {
        openConnection(connection -> {
            executeUpdate(connection, receiptsTable);
            executeUpdate(connection, itemsTable);
        });
    }

    private Set<String> getNames(final String sql) {
        final SQLFunction<ResultSet, Set<String>> loadReceiptItemsFunction = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new HashSet<>();
            }

            final Set<String> shopNameSet = new HashSet<>();
            do {
                final String shopName = resultSet.getString("name");
                shopNameSet.add(shopName);
            } while (resultSet.next());

            return shopNameSet;
        };

        return executeQueryAndOpenConnection(sql, loadReceiptItemsFunction);
    }
}
