package pl.crystalek.budgetapp;

import com.zaxxer.hikari.HikariDataSource;
import pl.crystalek.budgetapp.storage.util.SqlUtil;
import pl.crystalek.budgetapp.util.NumberUtil;

import java.util.HashMap;
import java.util.Map;

public class DataAnalyzer extends SqlUtil {
    public DataAnalyzer(final HikariDataSource database) {
        super(database);
    }

    public void printAnalysis() {
        final String sql = """
                SELECT i.name AS item_name, amount, price, money_division, u.name AS user_name
                FROM items i
                         JOIN receipts r ON r.id = i.receipt_id
                         JOIN users u ON u.id = r.who_paid_id
                WHERE shopping_date >= 1703980800000""";// AND shopping_date <= 1703898061000;
//                WHERE settled = false;""";

        executeQueryAndOpenConnection(sql, resultSet -> {
            final Map<String, Map<String, Double>> productCost = new HashMap<>();
            productCost.put("Ola", new HashMap<>());
            productCost.put("Jasio", new HashMap<>());

            while (resultSet.next()) {
                final String userName = resultSet.getString("user_name");
                final double amountForProduct = resultSet.getDouble("amount") * resultSet.getDouble("price");
                final double moneyDivision = resultSet.getDouble("money_division");
                final String itemName = resultSet.getString("item_name");

                final Map<String, Double> userMap = productCost.get(userName);
                userMap.put(itemName, userMap.getOrDefault(itemName, 0.0) + (amountForProduct - (amountForProduct * moneyDivision)));

                final String otherUserName = (userName.equals("Ola")) ? "Jasio" : "Ola";
                final Map<String, Double> otherUserMap = productCost.get(otherUserName);
                otherUserMap.put(itemName, otherUserMap.getOrDefault(itemName, 0.0) + (amountForProduct * moneyDivision));
            }

            final Map<String, Double> jasioMap = productCost.get("Jasio");
            jasioMap.entrySet().stream()
                    .filter(entry -> entry.getValue() != 0)
                    .sorted(Map.Entry.comparingByValue())
                    .forEach(entry -> System.out.println(entry.getKey() + " : " + NumberUtil.formatNumber(entry.getValue())));

            System.out.println(NumberUtil.formatNumber(jasioMap.values().stream().mapToDouble(Double::doubleValue).sum()));

            return null;
        });
    }
}
