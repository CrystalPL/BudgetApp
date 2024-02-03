package pl.crystalek.budgetapp.receipt;

import pl.crystalek.budgetapp.user.UserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ReceiptRepository {

    int createReceipt(final String shopName, final LocalDate shoppingDate, final UserDTO whoPaid, final boolean settled);

    void addItem(final List<ReceiptItem> receiptItemList, final int receiptId);

    List<ReceiptDTO> getReceiptDTOList();

    List<ReceiptItem> getReceiptItems(final int receiptId);

    void updateReceipt(final ReceiptDTO receipt);

    void updateReceiptItem(final ReceiptItem receiptItem);

    void deleteReceipt(final ReceiptDTO receipt);

    void deleteReceiptItem(final ReceiptItem receiptItem);

    Set<String> getShopNames();

    Set<String> getProductNames();

    void createTable();
}
