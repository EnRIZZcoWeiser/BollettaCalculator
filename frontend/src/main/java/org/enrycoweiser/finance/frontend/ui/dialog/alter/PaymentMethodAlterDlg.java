package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;

import java.util.Map;

public class PaymentMethodAlterDlg extends StandardAlterDlg {
    protected TextField nameTxt;
    protected Checkbox activeChk;

    protected boolean newItem;

    protected Runnable onConfirm;

    public PaymentMethodAlterDlg(String title, Map<String, Object> values, boolean newItem) {
        super(title);

        this.newItem = newItem;

        setFields(values);
    }

    protected void setFields(Map<String, Object> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.PAYMENT_METHOD_NAME) instanceof String s) {
            nameTxt.setValue(s);
        }

        if(values.get(FilterUtils.PAYMENT_METHOD_ACTIVE) instanceof Boolean b) {
            activeChk.setValue(b);
        }
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        nameTxt = new TextField(NameUtils.NAME);
        activeChk = new Checkbox(NameUtils.ACTIVE);

        layout.add(
                nameTxt,
                activeChk
        );

        return layout;
    }

    @Override
    protected void confirmButtonClicked() {
        String error = checkData();
        if(error != null) {
            NotificationHelper.showErrorMessage(error);
            return;
        }

        confirm();
    }

    protected void confirm() {

    }
}
