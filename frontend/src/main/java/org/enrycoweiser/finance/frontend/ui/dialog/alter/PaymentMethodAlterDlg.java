package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;
import org.enrycoweiser.finance.frontend.controller.PaymentMethodController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PaymentMethodAlterDlg extends StandardAlterDlg {
    protected TextField nameTxt;
    protected Checkbox activeChk;

    protected boolean newItem;
    protected Long idDto;

    protected Runnable onConfirm;

    public PaymentMethodAlterDlg(String title, Map<String, Object> values, boolean newItem) {
        super(title);

        this.newItem = newItem;

        setFields(values);
        setId(values);
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

        nameTxt = new TextField(NameUtils.NAME);
        activeChk = new Checkbox(NameUtils.ACTIVE);

        layout.add(
                nameTxt,
                activeChk
        );

        return layout;
    }

    protected void confirm() {
        Map<String, Object> data = createDataMap();

        PaymentMethodResponse res = PaymentMethodController.callAlterAPI(data);
        if(res == null) {
            NotificationHelper.showErrorMessage(NameUtils.PAYMENT_METHOD + NotificationUtils.SAVE_FAILED);
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
        if(nameTxt.getValue() == null || nameTxt.getValue().isBlank()) {
            return NotificationUtils.NAME_REQUIRED;
        }

        return null;
    }

    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);
        data.put(FilterUtils.PAYMENT_METHOD_NAME, nameTxt.getValue());
        data.put(FilterUtils.PAYMENT_METHOD_ACTIVE, activeChk.getValue());

        return data;
    }
}
