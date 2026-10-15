package org.enrycoweiser.finance.frontend.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.shared.utils.NameUtils;

@Route("")
public class DashboardView extends AppLayout {
    public DashboardView() {
        createLayout();
    }

    protected void createLayout() {
        createDrawer();
    }

    protected void createDrawer() {
        VerticalLayout drawer = new VerticalLayout();

        Button expenseBtn = createExpenseButton();
        expenseBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("/expense")));

        Button incomeBtn = createIncomeButton();
        incomeBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("/income")));

        Button paymentMethodBtn = createPaymentMethodButton();
        paymentMethodBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("/paymentMethod")));

        drawer.add(
                expenseBtn,
                incomeBtn,
                paymentMethodBtn
        );

        addToDrawer(drawer);
    }

    protected Button createExpenseButton() {
        Button btn = new Button(NameUtils.EXPENSE, VaadinIcon.MONEY.create());
        btn.setWidthFull();

        return btn;
    }

    protected Button createIncomeButton() {
        Button btn = new Button(NameUtils.INCOME, VaadinIcon.TRENDING_UP.create());
        btn.setWidthFull();

        return btn;
    }

    protected Button createPaymentMethodButton() {
        Button btn = new Button(NameUtils.PAYMENT_METHOD, VaadinIcon.CREDIT_CARD.create());
        btn.setWidthFull();

        return btn;
    }
}
