package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.SubscriptionController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.detail.SubscriptionDetailView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.SubscriptionAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.SubscriptionFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.SubscriptionResponse;
import org.enrycoweiser.finance.shared.dto.SubscriptionDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.*;

@Route(value = "/subscription", layout = DashboardView.class)
@PageTitle("Subscription")
public class SubscriptionListView extends StandardListView<SubscriptionDto> {

    protected Map<String, Object> filters;

    public SubscriptionListView() {
        super();

        filters = new HashMap<>();

        grid.addItemDoubleClickListener(e -> openDetail(e.getItem()));

        refreshBtn();
    }

    protected void openDetail(SubscriptionDto s) {
        getUI().ifPresent(ui -> ui.navigate(SubscriptionDetailView.class, s.getId()));
    }

    @Override
    protected void addBtn() {
        SubscriptionAlterDlg alterDlg = new SubscriptionAlterDlg(NameUtils.SUBSCRIPTION, null, true);
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

        SubscriptionResponse response = SubscriptionController.callDeleteAPI(id);
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
        SubscriptionResponse response = SubscriptionController.callRefreshAPI(filters);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.SUBSCRIPTION + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<SubscriptionDto> subscriptions = response.getItems();
        if(subscriptions == null) {
            subscriptions = new ArrayList<>();
        }

        grid.setItems(subscriptions);
    }

    @Override
    protected void editBtn() {
        Map<String, Object> data = createEditMap();
        if(data == null) {
            return;
        }

        SubscriptionAlterDlg alterDlg = new SubscriptionAlterDlg(NameUtils.SUBSCRIPTION, data, false);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        SubscriptionFilterDlg filterDlg = new SubscriptionFilterDlg(NameUtils.SUBSCRIPTION, filters);
        filterDlg.setOnConfirm(()  -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(SubscriptionDto::getName).setHeader(NameUtils.NAME);
        grid.addColumn(SubscriptionDto::getPrice).setHeader(NameUtils.PRICE);
        grid.addColumn(SubscriptionDto::getMonthlyAmount).setHeader(NameUtils.MONTHLY_AMOUNT);
        grid.addColumn(SubscriptionDto::getActive).setHeader(NameUtils.ACTIVE);
    }

    protected Map<String, Object> createEditMap() {
        Map<String, Object> data = new HashMap<>();

        SubscriptionDto s = grid.getSelectedItems().iterator().next();
        data.put(FilterUtils.ID, s.getId());
        data.put(FilterUtils.SUBSCRIPTION_NAME, s.getName());
        data.put(FilterUtils.SUBSCRIPTION_ACTIVE, s.getActive());

        return data;
    }
}
