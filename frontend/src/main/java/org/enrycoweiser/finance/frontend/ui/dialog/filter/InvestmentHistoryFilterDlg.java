package org.enrycoweiser.finance.frontend.ui.dialog.filter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
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
public class InvestmentHistoryFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;
    protected List<PaymentMethodDto> paymentMethods;

    protected DatePicker dateFromTxt;
    protected DatePicker dateToTxt;
    protected ComboBox<String> typeCmb;
    protected ComboBox<PaymentMethodDto> paymentMethodCmb;

    protected Runnable onConfirm;

    public InvestmentHistoryFilterDlg(String title, Map<String, Object> values, List<PaymentMethodDto> paymentMethods) {
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
        typeCmb = new ComboBox<>(NameUtils.TYPE);
        paymentMethodCmb = new ComboBox<>(NameUtils.PAYMENT_METHOD);

        layout.add(
                dateFromTxt,
                dateToTxt,
                typeCmb,
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
            filters.put(FilterUtils.INVESTMENT_HISTORY_DATE_FROM, dateFrom);
        }

        LocalDate dateTo = dateToTxt.getValue();
        if(dateTo != null) {
            filters.put(FilterUtils.INVESTMENT_HISTORY_DATE_TO, dateTo);
        }

        String type = typeCmb.getValue();
        if(type != null && !type.isBlank()) {
            filters.put(FilterUtils.INVESTMENT_HISTORY_TYPE, type);
        }

        PaymentMethodDto paymentMethod = paymentMethodCmb.getValue();
        if(paymentMethod != null) {
            filters.put(FilterUtils.INVESTMENT_HISTORY_PAYMENT_METHOD, paymentMethod);
        }
    }

    protected void valueComboBoxes() {
        valueTypeCombo();
        valuePaymentMethodCombo();
    }

    protected void valueTypeCombo() {
        typeCmb.setItems(
                StaticUtils.INVESTMENT_TYPES
        );
    }

    protected void valuePaymentMethodCombo() {
        paymentMethodCmb.setItems(
                paymentMethods
        );
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.INVESTMENT_HISTORY_DATE_FROM) instanceof LocalDate d) {
            dateFromTxt.setValue(d);
        }

        if(filters.get(FilterUtils.INVESTMENT_HISTORY_DATE_TO) instanceof LocalDate d) {
            dateToTxt.setValue(d);
        }

        if(filters.get(FilterUtils.INVESTMENT_HISTORY_TYPE) instanceof String s) {
            typeCmb.setValue(s);
        }

        if(filters.get(FilterUtils.INVESTMENT_HISTORY_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            paymentMethodCmb.setValue(p);
        }
    }
}
