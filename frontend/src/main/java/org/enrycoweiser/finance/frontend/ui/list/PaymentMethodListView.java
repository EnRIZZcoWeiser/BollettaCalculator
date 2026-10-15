package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.PaymentMethodController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.PaymentMethodAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.PaymentMethodFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.*;

@Route(value = "/paymentMethod", layout = DashboardView.class)
@PageTitle("Payment Method")
public class PaymentMethodListView extends StandardListView<PaymentMethodDto,
                                                            PaymentMethodAlterDlg,
                                                            PaymentMethodFilterDlg> {

    protected Map<String, Object> filters;

    public PaymentMethodListView() {
        super(PaymentMethodAlterDlg.class, PaymentMethodFilterDlg.class);

        filters = new HashMap<>();

        refreshBtn();
    }

    @Override
    protected void addBtn() {
        PaymentMethodAlterDlg alterDlg = new PaymentMethodAlterDlg(NameUtils.PAYMENT_METHOD, null, true);
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
        Set<PaymentMethodDto> items = grid.getSelectedItems();
        if(items == null || items.isEmpty()) {
            NotificationHelper.showErrorMessage(NotificationUtils.NO_ROWS);
            return ;
        }

        if(items.size() > 1) {
            NotificationHelper.showErrorMessage(NotificationUtils.MANY_ROWS);
            return;
        }

        Long id = grid.getSelectedItems().iterator().next().getId();

        PaymentMethodResponse response = PaymentMethodController.callDeleteAPI(id);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.PAYMENT_METHOD + NotificationUtils.DELETE_FAILED);
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
        PaymentMethodResponse response = PaymentMethodController.callRefreshAPI(filters);
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

        grid.setItems(methods);
    }

    @Override
    protected void editBtn() {
        Map<String, Object> data = createEditMap();
        if(data == null) {
            return;
        }

        PaymentMethodAlterDlg alterDlg = new PaymentMethodAlterDlg(NameUtils.PAYMENT_METHOD, data, false);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        PaymentMethodFilterDlg filterDlg = new PaymentMethodFilterDlg(NameUtils.PAYMENT_METHOD, filters);
        filterDlg.setOnConfirm(()  -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(PaymentMethodDto::getName).setHeader(NameUtils.NAME);
        grid.addColumn(PaymentMethodDto::getActive).setHeader(NameUtils.ACTIVE);
    }

    protected Map<String, Object> createEditMap() {
        Map<String, Object> data = new HashMap<>();

        Set<PaymentMethodDto> items = grid.getSelectedItems();
        if(items == null || items.isEmpty()) {
            NotificationHelper.showErrorMessage(NotificationUtils.NO_ROWS);
            return null;
        }

        if(items.size() > 1) {
            NotificationHelper.showErrorMessage(NotificationUtils.MANY_ROWS);
            return null;
        }

        PaymentMethodDto p = grid.getSelectedItems().iterator().next();
        data.put(FilterUtils.ID, p.getId());
        data.put(FilterUtils.PAYMENT_METHOD_NAME, p.getName());
        data.put(FilterUtils.PAYMENT_METHOD_ACTIVE, p.getActive());

        return data;
    }
}
