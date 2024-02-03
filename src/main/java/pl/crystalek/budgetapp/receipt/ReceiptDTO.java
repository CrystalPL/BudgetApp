package pl.crystalek.budgetapp.receipt;

import pl.crystalek.budgetapp.user.UserDTO;

import java.time.LocalDate;

public class ReceiptDTO {
    private final int receiptId;
    private final double amountForPurchase;
    private final UserDTO userDTO;
    private String shopName;
    private LocalDate shoppingDate;
    private boolean settled;

    public ReceiptDTO(final int receiptId, final double amountForPurchase, final String payerName, final String shopName, final LocalDate shoppingDate, final int payerId, final boolean settled) {
        this.receiptId = receiptId;
        this.amountForPurchase = amountForPurchase;
        this.shopName = shopName;
        this.shoppingDate = shoppingDate;
        this.settled = settled;
        this.userDTO = new UserDTO(payerName, payerId);
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public double getAmountForPurchase() {
        return amountForPurchase;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(final String shopName) {
        this.shopName = shopName;
    }

    public LocalDate getShoppingDate() {
        return shoppingDate;
    }

    public void setShoppingDate(final LocalDate shoppingDate) {
        this.shoppingDate = shoppingDate;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(final boolean settled) {
        this.settled = settled;
    }

    public int getReceiptId() {
        return receiptId;
    }
}
