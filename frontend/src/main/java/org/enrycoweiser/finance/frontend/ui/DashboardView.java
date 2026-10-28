package org.enrycoweiser.finance.frontend.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;
import org.enrycoweiser.finance.frontend.ui.list.AssetListView;
import org.enrycoweiser.finance.frontend.ui.list.ExpenseListView;
import org.enrycoweiser.finance.frontend.ui.list.IncomeListView;
import org.enrycoweiser.finance.frontend.ui.list.InvestmentListView;
import org.enrycoweiser.finance.frontend.ui.list.NetWorthListView;
import org.enrycoweiser.finance.frontend.ui.list.PaymentMethodListView;
import org.enrycoweiser.finance.frontend.ui.list.SubscriptionListView;
import org.enrycoweiser.finance.frontend.ui.list.TransferListView;
import org.enrycoweiser.finance.shared.utils.NameUtils;

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

    /*
        Logo + app name: a link back to the start page (HomeView).
     */
    protected Component createTitle() {
        Icon logo = VaadinIcon.PIGGY_BANK_COIN.create();
        logo.setSize("1.6rem");
        logo.getStyle().set("color", "var(--vaadin-text-color, currentColor)");

        H1 title = new H1(NameUtils.APP_NAME);
        title.getStyle()
                .set("font-size", "1.25rem")
                .set("margin", "0");

        RouterLink link = new RouterLink(HomeView.class);
        link.add(logo, title);
        link.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "0.5rem")
                .set("color", "inherit")
                .set("text-decoration", "none");

        return link;
    }

    protected void createDrawer() {
        SideNav nav = new SideNav();

        nav.addItem(
                createHomeItem(),
                createExpenseItem(),
                createIncomeItem(),
                createTransferItem(),
                createPaymentMethodItem(),
                createAssetItem(),
                createNetWorthItem(),
                createSubscriptionItem(),
                createInvestmentItem()
        );

        addToDrawer(nav);
    }

    protected SideNavItem createHomeItem() {
        return new SideNavItem(NameUtils.DASHBOARD, HomeView.class, VaadinIcon.HOME.create());
    }

    protected SideNavItem createExpenseItem() {
        return new SideNavItem(NameUtils.EXPENSE, ExpenseListView.class, VaadinIcon.MONEY.create());
    }

    protected SideNavItem createIncomeItem() {
        return new SideNavItem(NameUtils.INCOME, IncomeListView.class, VaadinIcon.TRENDING_UP.create());
    }

    protected SideNavItem createTransferItem() {
        return new SideNavItem(NameUtils.TRANSFER, TransferListView.class, VaadinIcon.EXCHANGE.create());
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

    protected SideNavItem createSubscriptionItem() {
        return new SideNavItem(NameUtils.SUBSCRIPTION, SubscriptionListView.class, VaadinIcon.CALENDAR.create());
    }

    protected SideNavItem createInvestmentItem() {
        return new SideNavItem(NameUtils.INVESTMENT, InvestmentListView.class, VaadinIcon.LINE_BAR_CHART.create());
    }
}
