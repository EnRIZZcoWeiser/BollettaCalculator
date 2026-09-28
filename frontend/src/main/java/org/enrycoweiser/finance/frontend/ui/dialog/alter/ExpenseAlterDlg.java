package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.formlayout.FormLayout;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;

import java.util.Map;

@Getter
@Setter
public class ExpenseAlterDlg extends StandardAlterDlg {

    protected boolean newItem;

    protected Runnable onConfirm;

    public ExpenseAlterDlg(String title, Map<String, Object> values, boolean newItem) {
        super(title);

        this.newItem = newItem;
    }

    @Override
    protected FormLayout createLayout() {
        return null;
    }

    @Override
    protected void confirmButtonClicked() {

    }
}
