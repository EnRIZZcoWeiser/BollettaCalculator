package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;
import org.enrycoweiser.finance.frontend.controller.InvestmentController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.InvestmentResponse;
import org.enrycoweiser.finance.shared.utils.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class InvestmentAlterDlg extends StandardAlterDlg {
    protected TextField nameTxt;
    protected ComboBox<String> categoryCmb;
    protected Checkbox activeChk;

    protected boolean newItem;
    protected Long idDto;

    protected Runnable onConfirm;

    public InvestmentAlterDlg(String title, Map<String, Object> values, boolean newItem) {
        super(title);

        this.newItem = newItem;

        valueComboBoxes();
        setFields(values);
        setId(values);
    }

    protected void valueComboBoxes() {
        valueCategoryCombo();
    }

    protected void valueCategoryCombo() {
        categoryCmb.setItems(
                StaticUtils.INVESTMENT_CATEGORIES
        );
    }

    protected void setFields(Map<String, Object> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.INVESTMENT_NAME) instanceof String s) {
            nameTxt.setValue(s);
        }

        if(values.get(FilterUtils.INVESTMENT_CATEGORY) instanceof String s) {
            try {
                categoryCmb.setValue(s);
            } catch (IllegalArgumentException iae) {
                categoryCmb.setValue(null);
            }
        }

        if(values.get(FilterUtils.INVESTMENT_ACTIVE) instanceof Boolean b) {
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
        categoryCmb = new ComboBox<>(NameUtils.CATEGORY);
        activeChk = new Checkbox(NameUtils.ACTIVE);

        layout.add(
                nameTxt,
                categoryCmb,
                activeChk
        );

        return layout;
    }

    protected void confirm() {
        Map<String, Object> data = createDataMap();

        InvestmentResponse res = InvestmentController.callAlterAPI(data);
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
        if(nameTxt.getValue() == null || nameTxt.getValue().isBlank()) {
            return NotificationUtils.NAME_REQUIRED;
        }

        if(categoryCmb.getValue() == null) {
            return NotificationUtils.CATEGORY_REQUIRED;
        }

        return null;
    }

    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);
        data.put(FilterUtils.INVESTMENT_NAME, nameTxt.getValue());
        data.put(FilterUtils.INVESTMENT_CATEGORY, categoryCmb.getValue());
        data.put(FilterUtils.INVESTMENT_ACTIVE, activeChk.getValue());

        return data;
    }
}
