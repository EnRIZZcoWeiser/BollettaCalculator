package org.enrycoweiser.finance.frontend.ui.detail;

import com.vaadin.flow.component.breadcrumbs.Breadcrumbs;
import com.vaadin.flow.component.breadcrumbs.BreadcrumbsItem;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.GetterController;
import org.enrycoweiser.finance.frontend.controller.SubscriptionController;
import org.enrycoweiser.finance.frontend.controller.SubscriptionHistoryController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.SubscriptionHistoryAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.SubscriptionHistoryFilterDlg;
import org.enrycoweiser.finance.frontend.ui.list.SubscriptionListView;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.api.response.SubscriptionHistoryResponse;
import org.enrycoweiser.finance.shared.api.response.SubscriptionResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.dto.SubscriptionDto;
import org.enrycoweiser.finance.shared.dto.SubscriptionHistoryDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

/*
    Detail of a single subscription: one row for each period paid (history).
 */
@Route(value = "/subscription/detail", layout = DashboardView.class)
@PageTitle("Subscription")
public class SubscriptionDetailView extends StandardListView<SubscriptionHistoryDto> implements HasUrlParameter<Long> {

    protected Map<String, Object> filters;
    protected List<PaymentMethodDto> paymentMethods;
    protected Long subscriptionId;

    protected BreadcrumbsItem subscriptionItem;

    public SubscriptionDetailView() {
        super();

        filters = new HashMap<>();
        paymentMethods = new ArrayList<>();

        addComponentAsFirst(createBreadcrumbs());

        grid.addItemDoubleClickListener(e -> openEdit(e.getItem()));
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        subscriptionId = parameter;

        loadSubscription();
        loadPaymentMethods();

        refreshBtn();
    }

    protected Breadcrumbs createBreadcrumbs() {
        Breadcrumbs breadcrumbs = new Breadcrumbs(Breadcrumbs.Mode.MANUAL);

        subscriptionItem = new BreadcrumbsItem("");

        breadcrumbs.add(
                new BreadcrumbsItem(NameUtils.SUBSCRIPTION, SubscriptionListView.class),
                subscriptionItem
        );

        return breadcrumbs;
    }

    protected void loadSubscription() {
        Map<String, Object> data = new HashMap<>();
        data.put(FilterUtils.ID, subscriptionId);

        SubscriptionResponse response = SubscriptionController.callRefreshAPI(data);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.SUBSCRIPTION + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        if(response.getItems() == null) {
            return;
        }

        for (SubscriptionDto s : response.getItems()) {
            if(subscriptionId.equals(s.getId())) {
                subscriptionItem.setText(s.getName());
            }
        }
    }

    protected void loadPaymentMethods() {
        PaymentMethodResponse response = GetterController.callGetAllPaymentMethodsAPI();
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.PAYMENT_METHOD + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<PaymentMethodDto> methods = response.getItems();
        if(methods == null) {
            methods = new ArrayList<>();
        }

        paymentMethods = methods;
    }

    @Override
    protected void addBtn() {
        SubscriptionHistoryAlterDlg alterDlg = new SubscriptionHistoryAlterDlg(NameUtils.SUBSCRIPTION, null, true, subscriptionId, paymentMethods);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void deleteBtn() {
        DeleteConfirmDlg deleteDlg = new DeleteConfirmDlg();
        deleteDlg.setOnConfirm(this::delete);

        deleteDlg.open();
    }

    protected void delete() {
        Long id = grid.getSelectedItems().iterator().next().getId();

        SubscriptionHistoryResponse response = SubscriptionHistoryController.callDeleteAPI(id);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.SUBSCRIPTION + NotificationUtils.DELETE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        NotificationHelper.showConfirmMessage(NotificationUtils.SUCCESS);

        this.refreshBtn();
    }

    @Override
    protected void refreshBtn() {
        if(subscriptionId == null) {
            return;
        }

        Map<String, Object> data = new HashMap<>(filters);
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_SUBSCRIPTION, subscriptionId);

        SubscriptionHistoryResponse response = SubscriptionHistoryController.callRefreshAPI(data);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.SUBSCRIPTION + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<SubscriptionHistoryDto> histories = response.getItems();
        if(histories == null) {
            histories = new ArrayList<>();
        }

        grid.setItems(histories);
    }

    @Override
    protected void editBtn() {
        openEdit(grid.getSelectedItems().iterator().next());
    }

    protected void openEdit(SubscriptionHistoryDto h) {
        Map<String, Object> data = createEditMap(h);

        SubscriptionHistoryAlterDlg alterDlg = new SubscriptionHistoryAlterDlg(NameUtils.SUBSCRIPTION, data, false, subscriptionId, paymentMethods);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        SubscriptionHistoryFilterDlg filterDlg = new SubscriptionHistoryFilterDlg(NameUtils.SUBSCRIPTION, filters, paymentMethods);
        filterDlg.setOnConfirm(() -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(h -> h.getStartDate() != null
                ? h.getStartDate().format(DateTimeFormatter.ofPattern("MM/yyyy"))
                : "").setHeader(NameUtils.START_MONTH);
        grid.addColumn(h -> h.getEndDate() != null
                ? h.getEndDate().format(DateTimeFormatter.ofPattern("MM/yyyy"))
                : "").setHeader(NameUtils.END_MONTH);
        grid.addColumn(SubscriptionHistoryDto::getMonths).setHeader(NameUtils.MONTHS);
        grid.addColumn(SubscriptionHistoryDto::getPrice).setHeader(NameUtils.PRICE);
        grid.addColumn(SubscriptionHistoryDto::getMonthlyAmount).setHeader(NameUtils.MONTHLY_AMOUNT);
        grid.addColumn(h -> h.getPaymentMethodDto() != null
                ? h.getPaymentMethodDto().getName()
                : "").setHeader(NameUtils.PAYMENT_METHOD);
    }

    protected Map<String, Object> createEditMap(SubscriptionHistoryDto h) {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, h.getId());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_START_DATE, h.getStartDate());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_END_DATE, h.getEndDate());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_PRICE, h.getPrice());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD, h.getPaymentMethodDto());

        return data;
    }
}
