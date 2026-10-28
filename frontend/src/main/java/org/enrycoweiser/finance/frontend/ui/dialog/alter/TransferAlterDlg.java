package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.PartialMatchMode;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;
import org.enrycoweiser.finance.frontend.controller.TransferController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.TransferResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class TransferAlterDlg extends StandardAlterDlg {
    protected DatePicker dateTxt;
    protected ComboBox<PaymentMethodDto> fromCmb;
    protected ComboBox<PaymentMethodDto> toCmb;
    protected BigDecimalField moneyTxt;
    protected TextField noteTxt;

    protected boolean newItem;
    protected Long idDto;
    protected List<PaymentMethodDto> paymentMethods;

    protected Runnable onConfirm;

    public TransferAlterDlg(String title, Map<String, Object> values, boolean newItem, List<PaymentMethodDto> paymentMethods) {
        super(title);

        this.newItem = newItem;
        this.paymentMethods = paymentMethods;

        valueComboBoxes();
        setFields(values);
        setId(values);
    }

    protected void valueComboBoxes() {
        valueFromCombo();
        valueToCombo();
    }

    protected void valueFromCombo() {
        fromCmb.setItems(
                paymentMethods
        );
        fromCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);
    }

    protected void valueToCombo() {
        toCmb.setItems(
                paymentMethods
        );
        toCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);
    }

    protected void setFields(Map<String, Object> values) {
        if(values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.TRANSFER_DATE) instanceof LocalDate d) {
            dateTxt.setValue(d);
        }

        if(values.get(FilterUtils.TRANSFER_FROM) instanceof PaymentMethodDto p) {
            try {
                fromCmb.setValue(p);
            } catch (IllegalArgumentException iae) {
                fromCmb.setValue(null);
            }
        }

        if(values.get(FilterUtils.TRANSFER_TO) instanceof PaymentMethodDto p) {
            try {
                toCmb.setValue(p);
            } catch (IllegalArgumentException iae) {
                toCmb.setValue(null);
            }
        }

        if(values.get(FilterUtils.TRANSFER_MONEY) instanceof BigDecimal b) {
            moneyTxt.setValue(b);
        }

        if(values.get(FilterUtils.TRANSFER_NOTE) instanceof String s) {
            noteTxt.setValue(s);
        }
    }

    protected void setId(Map<String, Object> values) {
        if(values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.ID) instanceof Long l) {
            this.idDto = l;
        }
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        dateTxt = new DatePicker(NameUtils.DATE);
        fromCmb = new ComboBox<>(NameUtils.FROM);
        toCmb = new ComboBox<>(NameUtils.TO);
        moneyTxt = new BigDecimalField(NameUtils.MONEY);
        noteTxt = new TextField(NameUtils.NOTE);

        layout.add(
                dateTxt,
                fromCmb,
                toCmb,
                moneyTxt,
                noteTxt
        );

        return layout;
    }

    protected void confirm() {
        Map<String, Object> data = createDataMap();

        TransferResponse res = TransferController.callAlterAPI(data);
        if(res == null) {
            NotificationHelper.showErrorMessage(NameUtils.TRANSFER + NotificationUtils.SAVE_FAILED);
            return;
        } else if(res.getStatus() != null && res.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(res.getErrorCode() + " - " + res.getError());
            return;
        }

        NotificationHelper.showConfirmMessage(NotificationUtils.SUCCESS);

        this.close();

        if(onConfirm != null) {
            onConfirm.run();
        }
    }

    protected String checkData() {
        if(dateTxt.getValue() == null) {
            return NotificationUtils.DATE_REQUIRED;
        }

        if(fromCmb.getValue() == null) {
            return NotificationUtils.FROM_REQUIRED;
        }

        if(toCmb.getValue() == null) {
            return NotificationUtils.TO_REQUIRED;
        }

        if(Objects.equals(fromCmb.getValue().getId(), toCmb.getValue().getId())) {
            return NotificationUtils.SAME_PAYMENT_METHOD;
        }

        if(moneyTxt.getValue() == null || moneyTxt.getValue().signum() <= 0) {
            return NotificationUtils.MONEY_REQUIRED;
        }

        return null;
    }

    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);

        /* mandatory data */
        data.put(FilterUtils.TRANSFER_DATE, dateTxt.getValue());
        data.put(FilterUtils.TRANSFER_FROM, fromCmb.getValue());
        data.put(FilterUtils.TRANSFER_TO, toCmb.getValue());
        data.put(FilterUtils.TRANSFER_MONEY, moneyTxt.getValue());

        /* others */
        String note = noteTxt.getValue();
        if(note != null && !note.isBlank()) {
            data.put(FilterUtils.TRANSFER_NOTE, note);
        }

        return data;
    }
}
