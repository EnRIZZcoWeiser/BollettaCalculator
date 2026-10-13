package org.enrycoweiser.finance.frontend.ui.dialog.filter;


import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardFilterDlg;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;

import java.util.Map;

@Getter
@Setter
public class PaymentMethodFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;

    protected TextField nameTxt;
    protected Checkbox activeChk;

    protected Runnable onConfirm;

    public PaymentMethodFilterDlg(String title, Map<String, Object> values) {
        super(title);

        this.filters = values;

        valueFields();
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        nameTxt = new TextField(NameUtils.NAME);
        activeChk = new Checkbox(NameUtils.ONLY_ACTIVE);

        layout.add(
                nameTxt,
                activeChk
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

        String name = nameTxt.getValue();
        if(name != null && !name.isBlank()) {
            filters.put(FilterUtils.PAYMENT_METHOD_NAME, name);
        }

        Boolean onlyActive = activeChk.getValue();
        if(onlyActive != null && onlyActive) {
            filters.put(FilterUtils.PAYMENT_METHOD_ONLY_ACTIVE, true);
        }
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.PAYMENT_METHOD_NAME) instanceof String s) {
            nameTxt.setValue(s);
        }

        if(filters.get(FilterUtils.PAYMENT_METHOD_ONLY_ACTIVE) instanceof Boolean b) {
            activeChk.setValue(b);
        }
    }
}
