package org.enrycoweiser.finance.frontend.ui.dialog.filter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardFilterDlg;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.StaticUtils;

import java.util.Map;

@Getter
@Setter
public class NetWorthEntryFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;

    protected ComboBox<String> categoryCmb;

    protected Runnable onConfirm;

    public NetWorthEntryFilterDlg(String title, Map<String, Object> values) {
        super(title);

        this.filters = values;

        valueComboBoxes();
        valueFields();
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        categoryCmb = new ComboBox<>(NameUtils.CATEGORY);

        layout.add(
                categoryCmb
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

        String category = categoryCmb.getValue();
        if(category != null && !category.isBlank()) {
            filters.put(FilterUtils.NET_WORTH_ENTRY_CATEGORY, category);
        }
    }

    protected void valueComboBoxes() {
        valueCategoryCombo();
    }

    protected void valueCategoryCombo() {
        categoryCmb.setItems(
                StaticUtils.NET_WORTH_CATEGORIES
        );
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.NET_WORTH_ENTRY_CATEGORY) instanceof String s) {
            categoryCmb.setValue(s);
        }
    }
}
