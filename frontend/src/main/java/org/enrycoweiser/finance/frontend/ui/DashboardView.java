package org.enrycoweiser.finance.frontend.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.ui.list.AssetListView;
import org.enrycoweiser.finance.frontend.ui.list.ExpenseListView;
import org.enrycoweiser.finance.frontend.ui.list.IncomeListView;
import org.enrycoweiser.finance.frontend.ui.list.NetWorthListView;
import org.enrycoweiser.finance.frontend.ui.list.PaymentMethodListView;
import org.enrycoweiser.finance.shared.utils.NameUtils;

@Route("")
public class DashboardView extends AppLayout {
    public DashboardView() {
        createLayout();
    }

    protected void createLayout() {
        createNavbar();
        createDrawer();
    }

    protected void createNavbar() {
        DrawerToggle toggle = new DrawerToggle();

        addToNavbar(
                toggle,
                createTitle()
        );
    }

    protected Component createTitle() {
        H1 title = new H1(NameUtils.APP_NAME);
        title.getStyle()
                .set("font-size", "1.25rem")
                .set("margin", "0");

        return title;
    }

    protected void createDrawer() {
        SideNav nav = new SideNav();

        nav.addItem(
                createExpenseItem(),
                createIncomeItem(),
                createPaymentMethodItem(),
                createAssetItem(),
                createNetWorthItem()
        );

        addToDrawer(nav);
    }

    protected SideNavItem createExpenseItem() {
        return new SideNavItem(NameUtils.EXPENSE, ExpenseListView.class, VaadinIcon.MONEY.create());
    }

    protected SideNavItem createIncomeItem() {
        return new SideNavItem(NameUtils.INCOME, IncomeListView.class, VaadinIcon.TRENDING_UP.create());
    }

    protected SideNavItem createPaymentMethodItem() {
        return new SideNavItem(NameUtils.PAYMENT_METHOD, PaymentMethodListView.class, VaadinIcon.CREDIT_CARD.create());
    }

    protected SideNavItem createAssetItem() {
        return new SideNavItem(NameUtils.ASSET, AssetListView.class, VaadinIcon.WALLET.create());
    }

    protected SideNavItem createNetWorthItem() {
        return new SideNavItem(NameUtils.NET_WORTH, NetWorthListView.class, VaadinIcon.CHART.create());
    }
}
