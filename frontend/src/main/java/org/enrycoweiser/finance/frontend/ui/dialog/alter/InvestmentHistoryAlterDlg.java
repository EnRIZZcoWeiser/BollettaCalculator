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
import org.enrycoweiser.finance.frontend.controller.InvestmentHistoryController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.InvestmentHistoryResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class InvestmentHistoryAlterDlg extends StandardAlterDlg {
    protected DatePicker dateTxt;
    protected ComboBox<String> typeCmb;
    protected BigDecimalField amountTxt;
    protected BigDecimalField feeTxt;
    protected BigDecimalField sharesTxt;
    protected ComboBox<PaymentMethodDto> paymentMethodCmb;
    protected TextField noteTxt;

    protected boolean newItem;
    protected Long idDto;
    protected Long investmentId;
    protected List<PaymentMethodDto> paymentMethods;

    protected Runnable onConfirm;

    public InvestmentHistoryAlterDlg(String title, Map<String, Object> values, boolean newItem, Long investmentId, List<PaymentMethodDto> paymentMethods) {
        super(title);

        this.newItem = newItem;
        this.investmentId = investmentId;
        this.paymentMethods = paymentMethods;

        valueComboBoxes();
        setDefaults();
        setFields(values);
        setId(values);
    }

    protected void valueComboBoxes() {
        valueTypeCombo();
        valuePaymentMethodCombo();
    }

    protected void valueTypeCombo() {
        typeCmb.setItems(
                StaticUtils.INVESTMENT_TYPES
        );
        typeCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);
    }

    protected void valuePaymentMethodCombo() {
        paymentMethodCmb.setItems(
                paymentMethods
        );
        paymentMethodCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);
    }

    /*
        A new operation starts as a buy with no fee.
     */
    protected void setDefaults() {
        if(!newItem) {
            return;
        }

        typeCmb.setValue(StaticUtils.INVESTMENT_BUY);
        feeTxt.setValue(BigDecimal.ZERO);
    }

    protected void setFields(Map<String, Object> values) {
        if(values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.INVESTMENT_HISTORY_DATE) instanceof LocalDate d) {
            dateTxt.setValue(d);
        }

        if(values.get(FilterUtils.INVESTMENT_HISTORY_TYPE) instanceof String s) {
            try {
                typeCmb.setValue(s);
            } catch (IllegalArgumentException iae) {
                typeCmb.setValue(null);
            }
        }

        if(values.get(FilterUtils.INVESTMENT_HISTORY_AMOUNT) instanceof BigDecimal b) {
            amountTxt.setValue(b);
        }

        if(values.get(FilterUtils.INVESTMENT_HISTORY_FEE) instanceof BigDecimal b) {
            feeTxt.setValue(b);
        }

        if(values.get(FilterUtils.INVESTMENT_HISTORY_SHARES) instanceof BigDecimal b) {
            sharesTxt.setValue(b);
        }

        if(values.get(FilterUtils.INVESTMENT_HISTORY_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            try {
                paymentMethodCmb.setValue(p);
            } catch (IllegalArgumentException iae) {
                paymentMethodCmb.setValue(null);
            }
        }

        if(values.get(FilterUtils.INVESTMENT_HISTORY_NOTE) instanceof String s) {
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
        typeCmb = new ComboBox<>(NameUtils.TYPE);
        amountTxt = new BigDecimalField(NameUtils.AMOUNT);
        feeTxt = new BigDecimalField(NameUtils.FEE);
        sharesTxt = new BigDecimalField(NameUtils.SHARES);
        paymentMethodCmb = new ComboBox<>(NameUtils.PAYMENT_METHOD);
        noteTxt = new TextField(NameUtils.NOTE);

        layout.add(
                dateTxt,
                typeCmb,
                amountTxt,
                feeTxt,
                sharesTxt,
                paymentMethodCmb,
                noteTxt
        );

        return layout;
    }

    @Override
    protected void confirm() {
        Map<String, Object> data = createDataMap();

        InvestmentHistoryResponse res = InvestmentHistoryController.callAlterAPI(data);
        if(res == null) {
            NotificationHelper.showErrorMessage(NameUtils.INVESTMENT + NotificationUtils.SAVE_FAILED);
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

    @Override
    protected String checkData() {
        if(dateTxt.getValue() == null) {
            return NotificationUtils.DATE_REQUIRED;
        }

        if(typeCmb.getValue() == null) {
            return NotificationUtils.TYPE_REQUIRED;
        }

        if(amountTxt.getValue() == null || amountTxt.getValue().signum() <= 0) {
            return NotificationUtils.AMOUNT_REQUIRED;
        }

        if(feeTxt.getValue() == null || feeTxt.getValue().signum() < 0) {
            return NotificationUtils.FEE_REQUIRED;
        }

        if(sharesTxt.getValue() == null || sharesTxt.getValue().signum() <= 0) {
            return NotificationUtils.SHARES_REQUIRED;
        }

        if(paymentMethodCmb.getValue() == null) {
            return NotificationUtils.PAYMENT_METHOD_REQUIRED;
        }

        return null;
    }

    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);
        data.put(FilterUtils.INVESTMENT_HISTORY_INVESTMENT, investmentId);

        /* mandatory data */
        data.put(FilterUtils.INVESTMENT_HISTORY_DATE, dateTxt.getValue());
        data.put(FilterUtils.INVESTMENT_HISTORY_TYPE, typeCmb.getValue());
        data.put(FilterUtils.INVESTMENT_HISTORY_AMOUNT, amountTxt.getValue());
        data.put(FilterUtils.INVESTMENT_HISTORY_FEE, feeTxt.getValue());
        data.put(FilterUtils.INVESTMENT_HISTORY_SHARES, sharesTxt.getValue());
        data.put(FilterUtils.INVESTMENT_HISTORY_PAYMENT_METHOD, paymentMethodCmb.getValue());

        /* others */
        String note = noteTxt.getValue();
        if(note != null && !note.isBlank()) {
            data.put(FilterUtils.INVESTMENT_HISTORY_NOTE, note);
        }

        return data;
    }
}
