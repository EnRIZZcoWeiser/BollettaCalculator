package org.enrycoweiser.finance.frontend.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteConfirmDlg extends Dialog {

    protected Runnable onConfirm;

    public DeleteConfirmDlg() {
        setHeaderTitle("Delete");

        setFooterButtons();

        add(
                createLayout()
        );
    }

    protected Component createLayout() {
        return createText();
    }

    protected void setFooterButtons() {
        Button goBackBtn = new Button("Go Back");
        goBackBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Button deleteBtn = new Button("Delete");
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        goBackBtn.addClickListener(event -> {
            goBack();
        });
        deleteBtn.addClickListener(event -> {
            delete();
        });

        this.getFooter().add(goBackBtn, deleteBtn);
    }

    private Component createText() {
        return new Text("Are you sure you want to delete this?");
    }

    private void delete() {
        this.close();

        if(onConfirm != null) {
            onConfirm.run();
        }
    }

    private void goBack() {
        this.close();
    }
}
