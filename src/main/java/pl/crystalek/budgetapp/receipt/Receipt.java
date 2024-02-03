package pl.crystalek.budgetapp.receipt;

import lombok.AllArgsConstructor;
import pl.crystalek.budgetapp.user.UserDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Receipt {
    private final List<ReceiptItem> receiptItemList;
    private final String shopName;
    private final LocalDate shoppingDate;
    private final UserDTO whoPaid;
    private final boolean settled;
    private int receiptId;

    public Receipt(final String shopName, final LocalDate shoppingDate, final UserDTO whoPaid, final boolean settled) {
        this.receiptItemList = new ArrayList<>();
        this.shopName = shopName;
        this.shoppingDate = shoppingDate;
        this.whoPaid = whoPaid;
        this.settled = settled;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(final int receiptId) {
        this.receiptId = receiptId;
    }

    public void addReceiptItem(final ReceiptItem receiptItem) {
        receiptItemList.add(receiptItem);
    }

    public String getShopName() {
        return shopName;
    }

    public LocalDate getShoppingDate() {
        return shoppingDate;
    }

    public double getTotalAmount() {
        //dodac liczenie amount * price
        return receiptItemList.stream()
                .map(ReceiptItem::getPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public UserDTO getWhoPaid() {
        return whoPaid;
    }

    public boolean isSettled() {
        return settled;
    }

    public List<ReceiptItem> getReceiptItemList() {
        return receiptItemList;
    }
}
