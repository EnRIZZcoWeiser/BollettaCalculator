package org.enrycoweiser.finance.frontend.ui.dialog.filter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardFilterDlg;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SubscriptionHistoryFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;
    protected List<PaymentMethodDto> paymentMethods;

    protected ComboBox<PaymentMethodDto> paymentMethodCmb;

    protected Runnable onConfirm;

    public SubscriptionHistoryFilterDlg(String title, Map<String, Object> values, List<PaymentMethodDto> paymentMethods) {
        super(title);

        this.filters = values;
        this.paymentMethods = paymentMethods;

        valueComboBoxes();
        valueFields();
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        paymentMethodCmb = new ComboBox<>(NameUtils.PAYMENT_METHOD);

        layout.add(
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

        PaymentMethodDto paymentMethod = paymentMethodCmb.getValue();
        if(paymentMethod != null) {
            filters.put(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD, paymentMethod);
        }
    }

    protected void valueComboBoxes() {
        valuePaymentMethodCombo();
    }

    protected void valuePaymentMethodCombo() {
        paymentMethodCmb.setItems(
                paymentMethods
        );
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            paymentMethodCmb.setValue(p);
        }
    }
}
