package org.enrycoweiser.finance.frontend.ui.dialog.filter;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardFilterDlg;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.StaticUtils;

import java.util.Map;

@Getter
@Setter
public class InvestmentFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;

    protected TextField nameTxt;
    protected ComboBox<String> categoryCmb;
    protected Checkbox activeChk;

    protected Runnable onConfirm;

    public InvestmentFilterDlg(String title, Map<String, Object> values) {
        super(title);

        this.filters = values;

        valueComboBoxes();
        valueFields();
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        nameTxt = new TextField(NameUtils.NAME);
        categoryCmb = new ComboBox<>(NameUtils.CATEGORY);
        activeChk = new Checkbox(NameUtils.ONLY_ACTIVE);

        layout.add(
                nameTxt,
                categoryCmb,
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
            filters.put(FilterUtils.INVESTMENT_NAME, name);
        }

        String category = categoryCmb.getValue();
        if(category != null && !category.isBlank()) {
            filters.put(FilterUtils.INVESTMENT_CATEGORY, category);
        }

        Boolean onlyActive = activeChk.getValue();
        if(onlyActive != null && onlyActive) {
            filters.put(FilterUtils.INVESTMENT_ONLY_ACTIVE, true);
        }
    }

    protected void valueComboBoxes() {
        valueCategoryCombo();
    }

    protected void valueCategoryCombo() {
        categoryCmb.setItems(
                StaticUtils.INVESTMENT_CATEGORIES
        );
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.INVESTMENT_NAME) instanceof String s) {
            nameTxt.setValue(s);
        }

        if(filters.get(FilterUtils.INVESTMENT_CATEGORY) instanceof String s) {
            categoryCmb.setValue(s);
        }

        if(filters.get(FilterUtils.INVESTMENT_ONLY_ACTIVE) instanceof Boolean b) {
            activeChk.setValue(b);
        }
    }
}
