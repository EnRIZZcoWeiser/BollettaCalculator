package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.ExpenseController;
import org.enrycoweiser.finance.frontend.controller.GetterController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.ExpenseAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.ExpenseFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.ExpenseResponse;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.dto.ExpenseDto;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.*;

@Route(value = "/expense", layout = DashboardView.class)
@PageTitle("Expense")
public class ExpenseListView extends StandardListView<ExpenseDto> {

    protected Map<String, Object> filters;
    protected List<PaymentMethodDto> paymentMethods;

    public ExpenseListView() {
        super();

        filters = new HashMap<>();
        paymentMethods = new ArrayList<>();

        loadPaymentMethods();

        refreshBtn();
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
        ExpenseAlterDlg alterDlg = new ExpenseAlterDlg(NameUtils.EXPENSE, null, true, paymentMethods);
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

        ExpenseResponse response = ExpenseController.callDeleteAPI(id);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.EXPENSE + NotificationUtils.DELETE_FAILED);
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
        ExpenseResponse response = ExpenseController.callRefreshAPI(filters);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.EXPENSE + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<ExpenseDto> expenses = response.getItems();
        if(expenses == null) {
            expenses = new ArrayList<>();
        }

        grid.setItems(expenses);
    }

    @Override
    protected void editBtn() {
        Map<String, Object> data = createEditMap();
        if(data == null) {
            return;
        }

        ExpenseAlterDlg alterDlg = new ExpenseAlterDlg(NameUtils.EXPENSE, data, false, paymentMethods);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        ExpenseFilterDlg filterDlg = new ExpenseFilterDlg(NameUtils.EXPENSE, filters, paymentMethods);
        filterDlg.setOnConfirm(() -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(ExpenseDto::getDate).setHeader(NameUtils.DATE);
        grid.addColumn(ExpenseDto::getMoney).setHeader(NameUtils.MONEY);
        grid.addColumn(ExpenseDto::getLocation).setHeader(NameUtils.LOCATION);
        grid.addColumn(ExpenseDto::getStore).setHeader(NameUtils.STORE);
        grid.addColumn(ExpenseDto::getCategory).setHeader(NameUtils.CATEGORY);
        grid.addColumn(e -> e.getPaymentMethodDto() != null
                ? e.getPaymentMethodDto().getName()
                : "").setHeader(NameUtils.PAYMENT_METHOD);
        grid.addColumn(ExpenseDto::getNote).setHeader(NameUtils.NOTE);
    }

    protected Map<String, Object> createEditMap() {
        Map<String, Object> data = new HashMap<>();


        ExpenseDto e = grid.getSelectedItems().iterator().next();
        data.put(FilterUtils.ID, e.getId());
        data.put(FilterUtils.EXPENSE_DATE, e.getDate());
        data.put(FilterUtils.EXPENSE_MONEY, e.getMoney());
        data.put(FilterUtils.EXPENSE_LOCATION, e.getLocation());
        data.put(FilterUtils.EXPENSE_STORE, e.getStore());
        data.put(FilterUtils.EXPENSE_CATEGORY, e.getCategory());
        data.put(FilterUtils.EXPENSE_PAYMENT_METHOD, e.getPaymentMethodDto());
        data.put(FilterUtils.EXPENSE_NOTE, e.getNote());

        return data;
    }
}
