package org.enrycoweiser.finance.frontend.ui.dialog.filter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardFilterDlg;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.StaticUtils;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class ExpenseFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;

    protected DatePicker dateFromTxt;
    protected DatePicker dateToTxt;
    protected ComboBox<String> categoryCmb;
    protected TextField locationTxt;
    protected TextField storeTxt;
    protected ComboBox<PaymentMethodDto> paymentMethodCmb;

    protected Runnable onConfirm;

    public ExpenseFilterDlg(String title, Map<String, Object> values) {
        super(title);

        this.filters = values;

        valueComboBoxes();
        valueFields();
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        dateFromTxt = new DatePicker(NameUtils.DATE_FROM);
        dateToTxt = new DatePicker(NameUtils.DATE_TO);
        categoryCmb = new ComboBox<>(NameUtils.CATEGORY);
        locationTxt = new TextField(NameUtils.LOCATION);
        storeTxt = new TextField(NameUtils.STORE);
        paymentMethodCmb = new ComboBox<>(NameUtils.PAYMENT_METHOD);

        layout.add(
                dateFromTxt,
                dateToTxt,
                categoryCmb,
                locationTxt,
                storeTxt,
                paymentMethodCmb
        );

        return layout;
    }

    @Override
    protected void confirmButtonClicked() {
        confirm();

        this.close();

        if(onConfirm != null) {
            onConfirm.run();
        }
    }

    protected void confirm() {
        filters.clear();


    }

    protected void valueComboBoxes() {
        valueCategoryCombo();
        valuePaymentMethodCombo();
    }

    protected void valueCategoryCombo() {
        categoryCmb.setItems(
                StaticUtils.EXPENSE_CATEGORIES
        );
    }

    protected void valuePaymentMethodCombo() {
        // ON HOLD - Not Available on version 1.0.0
        paymentMethodCmb.clear();
        paymentMethodCmb.setItems(
                new ArrayList<>()
        );
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.EXPENSE_DATE_FROM) instanceof Date d) {
            dateFromTxt.setValue(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        if(filters.get(FilterUtils.EXPENSE_DATE_TO) instanceof Date d) {
            dateToTxt.setValue(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        if(filters.get(FilterUtils.EXPENSE_CATEGORY) instanceof String s) {
            categoryCmb.setValue(s);
        }

        if(filters.get(FilterUtils.EXPENSE_LOCATION) instanceof String s) {
            locationTxt.setValue(s);
        }

        if(filters.get(FilterUtils.EXPENSE_STORE) instanceof String s) {
            storeTxt.setValue(s);
        }
    }
}
