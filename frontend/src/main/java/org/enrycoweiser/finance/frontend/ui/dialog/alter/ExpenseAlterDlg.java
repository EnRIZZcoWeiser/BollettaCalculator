package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;
import org.enrycoweiser.finance.frontend.controller.ExpenseController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.ExpenseResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ExpenseAlterDlg extends StandardAlterDlg {
    protected DatePicker dateTxt;
    protected BigDecimalField moneyTxt;
    protected ComboBox<String> categoryCmb;
    protected TextField locationTxt;
    protected TextField storeTxt;
    protected ComboBox<PaymentMethodDto> paymentMethodCmb;
    protected TextField noteTxt;

    protected boolean newItem;
    protected Long idDto;

    protected Runnable onConfirm;

    public ExpenseAlterDlg(String title, Map<String, Object> values, boolean newItem) {
        super(title);

        this.newItem = newItem;

        valueComboBoxes();
        setFields(values);
        setId(values);
    }

    protected void valueComboBoxes() {
        valueCategoryCombo();
        valuePaymentMethodCombo();
    }

    protected void valueCategoryCombo() {
        categoryCmb.setItems(
                StaticUtils.INCOME_CATEGORIES
        );
    }

    protected void valuePaymentMethodCombo() {
        // TODO - don't know how to retrieve data rn
        paymentMethodCmb.setItems(
                new ArrayList<>()
        );
    }

    protected void setFields(Map<String, Object> values) {
        if(values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.EXPENSE_DATE) instanceof LocalDate d) {
            dateTxt.setValue(d);
        }

        if(values.get(FilterUtils.EXPENSE_MONEY) instanceof BigDecimal b) {
            moneyTxt.setValue(b);
        }

        if(values.get(FilterUtils.EXPENSE_LOCATION) instanceof String s) {
            locationTxt.setValue(s);
        }

        if(values.get(FilterUtils.EXPENSE_STORE) instanceof String s) {
            storeTxt.setValue(s);
        }

        if(values.get(FilterUtils.EXPENSE_CATEGORY) instanceof String s) {
            try {
                categoryCmb.setValue(s);
            } catch (IllegalArgumentException iae) {
                categoryCmb.setValue(null);
            }
        }

        if(values.get(FilterUtils.EXPENSE_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            try {
                paymentMethodCmb.setValue(p);
            } catch (IllegalArgumentException iae) {
                paymentMethodCmb.setValue(null);
            }
        }

        if(values.get(FilterUtils.EXPENSE_NOTE) instanceof String s) {
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
        moneyTxt = new BigDecimalField(NameUtils.MONEY);
        locationTxt = new TextField(NameUtils.LOCATION);
        storeTxt = new TextField(NameUtils.STORE);
        categoryCmb = new ComboBox<>(NameUtils.CATEGORY);
        paymentMethodCmb = new ComboBox<>(NameUtils.PAYMENT_METHOD);
        noteTxt = new TextField(NameUtils.NOTE);

        layout.add(
                dateTxt,
                moneyTxt,
                locationTxt,
                storeTxt,
                categoryCmb,
                paymentMethodCmb,
                noteTxt
        );

        return layout;
    }

    @Override
    protected void confirm() {
        Map<String, Object> data = createDataMap();

        ExpenseResponse res = ExpenseController.callAlterAPI(data);
        if(res == null) {
            NotificationHelper.showErrorMessage(NameUtils.EXPENSE + NotificationUtils.SAVE_FAILED);
            return;
        } else if(res.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(res.getError());
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

        if(moneyTxt.getValue() == null || moneyTxt.getValue().signum() <= 0) {
            return NotificationUtils.MONEY_REQUIRED;
        }

        if(locationTxt.getValue() == null || locationTxt.getValue().isBlank()) {
            return NotificationUtils.LOCATION_REQUIRED;
        }

        if(storeTxt.getValue() == null || storeTxt.getValue().isBlank()) {
            return NotificationUtils.STORE_REQUIRED;
        }

        if(categoryCmb.getValue() == null) {
            return NotificationUtils.CATEGORY_REQUIRED;
        }

        if(paymentMethodCmb.getValue() == null) {
            return NotificationUtils.PAYMENT_METHOD_REQUIRED;
        }

        return null;
    }

    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);

        /* mandatory data */
        data.put(FilterUtils.EXPENSE_DATE, dateTxt.getValue());
        data.put(FilterUtils.EXPENSE_MONEY, moneyTxt.getValue());
        data.put(FilterUtils.EXPENSE_LOCATION, locationTxt.getValue());
        data.put(FilterUtils.EXPENSE_STORE, storeTxt.getValue());
        data.put(FilterUtils.EXPENSE_CATEGORY, categoryCmb.getValue());
        data.put(FilterUtils.EXPENSE_PAYMENT_METHOD, paymentMethodCmb.getValue());

        /* others */
        String note = noteTxt.getValue();
        if(note != null && !note.isBlank()) {
            data.put(FilterUtils.EXPENSE_NOTE, note);
        }

        return data;
    }
}
