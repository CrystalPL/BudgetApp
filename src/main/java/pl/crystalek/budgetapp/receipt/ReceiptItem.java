package pl.crystalek.budgetapp.receipt;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.category.CategoryDTO;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReceiptItem {
    String productName;
    double amount;
    double price;
    CategoryDTO category;
    double moneyDivision;
    int itemId;

    public ReceiptItem(final String productName, final double amount, final double price, final CategoryDTO category, final double moneyDivision) {
        this.productName = productName;
        this.amount = amount;
        this.price = price;
        this.category = category;
        this.moneyDivision = moneyDivision;
    }

    public ReceiptItem(final String productName, final double amount, final double price, final CategoryDTO category, final double moneyDivision, final int itemId) {
        this.productName = productName;
        this.amount = amount;
        this.price = price;
        this.category = category;
        this.moneyDivision = moneyDivision;
        this.itemId = itemId;
    }
}
