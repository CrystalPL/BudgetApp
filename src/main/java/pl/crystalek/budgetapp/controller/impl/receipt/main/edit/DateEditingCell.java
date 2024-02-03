package pl.crystalek.budgetapp.controller.impl.receipt.main.edit;


import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.budgetapp.io.ResourceRepository;
import pl.crystalek.budgetapp.receipt.ReceiptDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DateEditingCell extends TableCell<ReceiptDTO, LocalDate> {
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
    ResourceRepository resourceRepository;

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);

        final DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.getStylesheets().add(resourceRepository.getStyleSheet("datepicker"));
        datePicker.setShowWeekNumbers(false);
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((event) -> commitEdit(datePicker.getValue()));

        setGraphic(datePicker);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getItem().format(DATE_FORMATTER));
        setGraphic(null);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item.format(DATE_FORMATTER));
            setGraphic(null);
        }
    }
}