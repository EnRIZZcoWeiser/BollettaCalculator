package org.enrycoweiser.finance.frontend.base;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;

public abstract class StandardAlterDlg extends Dialog {

    public StandardAlterDlg(String title) {
        this.setHeaderTitle(title);

        setFooterButtons();

        add(
                createLayout()
        );
    }

    protected abstract FormLayout createLayout();

    private void setFooterButtons() {
        Button confirmButton = new Button("Confirm");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        confirmButton.addClickListener(event -> {
            confirmButtonClicked();
        });
        cancelButton.addClickListener(event -> {
            this.close();
        });

        this.getFooter().add(cancelButton, confirmButton);
    }

    protected abstract void confirmButtonClicked();
}
