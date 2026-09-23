package org.enrycoweiser.finance.frontend.base;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;

public abstract class StandardFilterDlg extends Dialog {

    protected boolean isConfirm;

    public StandardFilterDlg(String title) {
        this.setHeaderTitle(title);

        setFooterButtons(this);

        add(
                createLayout()
        );
    }

    protected abstract FormLayout createLayout();

    private void setFooterButtons(StandardFilterDlg standardFilterDlg) {
        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        confirmButton.addClickListener(event -> {
            isConfirm = true;
            confirmButtonClicked();
        });
        cancelButton.addClickListener(event -> {
            isConfirm = false;
            this.close();
        });

        this.getFooter().add(confirmButton, cancelButton);
    }

    protected abstract void confirmButtonClicked();
}
