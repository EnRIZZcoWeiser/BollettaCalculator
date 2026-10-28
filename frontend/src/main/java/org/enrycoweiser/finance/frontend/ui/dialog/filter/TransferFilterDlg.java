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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TransferFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;
    protected List<PaymentMethodDto> paymentMethods;

    protected DatePicker dateFromTxt;
    protected DatePicker dateToTxt;
    protected ComboBox<PaymentMethodDto> fromCmb;
    protected ComboBox<PaymentMethodDto> toCmb;
    protected ComboBox<PaymentMethodDto> paymentMethodCmb;

    protected Runnable onConfirm;

    public TransferFilterDlg(String title, Map<String, Object> values, List<PaymentMethodDto> paymentMethods) {
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
        fromCmb = new ComboBox<>(NameUtils.FROM);
        toCmb = new ComboBox<>(NameUtils.TO);
        paymentMethodCmb = new ComboBox<>(NameUtils.PAYMENT_METHOD);
        paymentMethodCmb.setHelperText(NameUtils.FROM + " / " + NameUtils.TO);

        layout.add(
                dateFromTxt,
                dateToTxt,
                fromCmb,
                toCmb,
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
            filters.put(FilterUtils.TRANSFER_DATE_FROM, dateFrom);
        }

        LocalDate dateTo = dateToTxt.getValue();
        if(dateTo != null) {
            filters.put(FilterUtils.TRANSFER_DATE_TO, dateTo);
        }

        PaymentMethodDto from = fromCmb.getValue();
        if(from != null) {
            filters.put(FilterUtils.TRANSFER_FROM, from);
        }

        PaymentMethodDto to = toCmb.getValue();
        if(to != null) {
            filters.put(FilterUtils.TRANSFER_TO, to);
        }

        PaymentMethodDto paymentMethod = paymentMethodCmb.getValue();
        if(paymentMethod != null) {
            filters.put(FilterUtils.TRANSFER_PAYMENT_METHOD, paymentMethod);
        }
    }

    protected void valueComboBoxes() {
        valuePaymentMethodCombos();
    }

    protected void valuePaymentMethodCombos() {
        fromCmb.setItems(
                paymentMethods
        );
        toCmb.setItems(
                paymentMethods
        );
        paymentMethodCmb.setItems(
                paymentMethods
        );
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.TRANSFER_DATE_FROM) instanceof LocalDate d) {
            dateFromTxt.setValue(d);
        }

        if(filters.get(FilterUtils.TRANSFER_DATE_TO) instanceof LocalDate d) {
            dateToTxt.setValue(d);
        }

        if(filters.get(FilterUtils.TRANSFER_FROM) instanceof PaymentMethodDto p) {
            fromCmb.setValue(p);
        }

        if(filters.get(FilterUtils.TRANSFER_TO) instanceof PaymentMethodDto p) {
            toCmb.setValue(p);
        }

        if(filters.get(FilterUtils.TRANSFER_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            paymentMethodCmb.setValue(p);
        }
    }
}
