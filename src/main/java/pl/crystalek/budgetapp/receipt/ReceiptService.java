package pl.crystalek.budgetapp.receipt;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.user.UserDTO;
import pl.crystalek.budgetapp.util.ThreadUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReceiptService {
    ReceiptRepository receiptRepository;

    public void fetchReceipts(final Consumer<List<ReceiptDTO>> consumer) {
        ThreadUtil.runAsync(() -> ThreadUtil.runInGUIThread(() -> consumer.accept(receiptRepository.getReceiptDTOList())));
    }

    public void deleteReceipt(final ReceiptDTO receipt) {
        ThreadUtil.runAsync(() -> receiptRepository.deleteReceipt(receipt));
    }

    public void deleteReceiptItem(final ReceiptItem receiptItem) {
        ThreadUtil.runAsync(() -> receiptRepository.deleteReceiptItem(receiptItem));
    }

    public void createReceipt(final String shopName, final LocalDate shoppingDate, final UserDTO whoPaid, final boolean settled, final Consumer<Integer> receiptIdConsumer) {
        ThreadUtil.runAsync(() -> {
            final int receiptId = receiptRepository.createReceipt(shopName, shoppingDate, whoPaid, settled);
            receiptIdConsumer.accept(receiptId);
        });
    }

    public void addItem(final List<ReceiptItem> receiptItemList, final int receiptId) {
        ThreadUtil.runAsync(() -> receiptRepository.addItem(receiptItemList, receiptId));
    }

    public void updateReceiptItem(final ReceiptItem receiptItem) {
        if (receiptItem.getItemId() == 0) {
            return;
        }

        ThreadUtil.runAsync(() -> receiptRepository.updateReceiptItem(receiptItem));
    }

    public void fetchReceiptItems(final int receiptId, final Consumer<List<ReceiptItem>> consumer) {
        ThreadUtil.runAsync(() -> ThreadUtil.runInGUIThread(() -> consumer.accept(receiptRepository.getReceiptItems(receiptId))));
    }

    public void fetchShopNames(final Consumer<Set<String>> consumer) {
        ThreadUtil.runAsync(() -> ThreadUtil.runInGUIThread(() -> consumer.accept(receiptRepository.getShopNames())));
    }

    public void fetchProductNames(final Consumer<Set<String>> consumer) {
        ThreadUtil.runAsync(() -> ThreadUtil.runInGUIThread(() -> consumer.accept(receiptRepository.getProductNames())));
    }

    public void updateReceipt(final ReceiptDTO receipt) {
        ThreadUtil.runAsync(() -> receiptRepository.updateReceipt(receipt));
    }
}
