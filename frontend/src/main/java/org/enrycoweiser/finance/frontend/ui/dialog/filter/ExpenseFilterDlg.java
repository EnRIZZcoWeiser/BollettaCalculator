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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ExpenseFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;
    protected List<PaymentMethodDto> paymentMethods;

    protected DatePicker dateFromTxt;
    protected DatePicker dateToTxt;
    protected ComboBox<String> categoryCmb;
    protected TextField locationTxt;
    protected TextField storeTxt;
    protected ComboBox<PaymentMethodDto> paymentMethodCmb;

    protected Runnable onConfirm;

    public ExpenseFilterDlg(String title, Map<String, Object> values, List<PaymentMethodDto> paymentMethods) {
        super(title);

        this.filters = values;
        this.paymentMethods = paymentMethods;

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

        LocalDate dateFrom = dateFromTxt.getValue();
        if(dateFrom != null) {
            filters.put(FilterUtils.EXPENSE_DATE_FROM, dateFrom);
        }

        LocalDate dateTo = dateToTxt.getValue();
        if(dateTo != null) {
            filters.put(FilterUtils.EXPENSE_DATE_TO, dateTo);
        }

        String category = categoryCmb.getValue();
        if(category != null && !category.isBlank()) {
            filters.put(FilterUtils.EXPENSE_CATEGORY, category);
        }

        String location = locationTxt.getValue();
        if(location != null && !location.isBlank()) {
            filters.put(FilterUtils.EXPENSE_LOCATION, location);
        }

        String store = storeTxt.getValue();
        if(store != null && !store.isBlank()) {
            filters.put(FilterUtils.EXPENSE_STORE, store);
        }

        PaymentMethodDto paymentMethod = paymentMethodCmb.getValue();
        if(paymentMethod != null) {
            filters.put(FilterUtils.EXPENSE_PAYMENT_METHOD, paymentMethod);
        }
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
        paymentMethodCmb.clear();
        paymentMethodCmb.setItems(
                paymentMethods
        );
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.EXPENSE_DATE_FROM) instanceof LocalDate d) {
            dateFromTxt.setValue(d);
        }

        if(filters.get(FilterUtils.EXPENSE_DATE_TO) instanceof LocalDate d) {
            dateToTxt.setValue(d);
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

        if(filters.get(FilterUtils.EXPENSE_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            paymentMethodCmb.setValue(p);
        }
    }
}