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
import org.enrycoweiser.finance.frontend.controller.InvestmentController;
import org.enrycoweiser.finance.frontend.controller.InvestmentHistoryController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.InvestmentHistoryAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.InvestmentHistoryFilterDlg;
import org.enrycoweiser.finance.frontend.ui.list.InvestmentListView;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.api.response.InvestmentHistoryResponse;
import org.enrycoweiser.finance.shared.api.response.InvestmentResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.dto.InvestmentDto;
import org.enrycoweiser.finance.shared.dto.InvestmentHistoryDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.util.*;

/*
    Detail of a single investment: one row for each buy or sell (history).
 */
@Route(value = "/investment/detail", layout = DashboardView.class)
@PageTitle("Investment")
public class InvestmentDetailView extends StandardListView<InvestmentHistoryDto> implements HasUrlParameter<Long> {

    protected Map<String, Object> filters;
    protected List<PaymentMethodDto> paymentMethods;
    protected Long investmentId;

    protected BreadcrumbsItem investmentItem;

    public InvestmentDetailView() {
        super();

        filters = new HashMap<>();
        paymentMethods = new ArrayList<>();

        addComponentAsFirst(createBreadcrumbs());

        grid.addItemDoubleClickListener(e -> openEdit(e.getItem()));
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        investmentId = parameter;

        loadInvestment();
        loadPaymentMethods();

        refreshBtn();
    }

    protected Breadcrumbs createBreadcrumbs() {
        Breadcrumbs breadcrumbs = new Breadcrumbs(Breadcrumbs.Mode.MANUAL);

        investmentItem = new BreadcrumbsItem("");

        breadcrumbs.add(
                new BreadcrumbsItem(NameUtils.INVESTMENT, InvestmentListView.class),
                investmentItem
        );

        return breadcrumbs;
    }

    protected void loadInvestment() {
        Map<String, Object> data = new HashMap<>();
        data.put(FilterUtils.ID, investmentId);

        InvestmentResponse response = InvestmentController.callRefreshAPI(data);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.INVESTMENT + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        if(response.getItems() == null) {
            return;
        }

        for (InvestmentDto s : response.getItems()) {
            if(investmentId.equals(s.getId())) {
                investmentItem.setText(s.getName());
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
        InvestmentHistoryAlterDlg alterDlg = new InvestmentHistoryAlterDlg(NameUtils.INVESTMENT, null, true, investmentId, paymentMethods);
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

        InvestmentHistoryResponse response = InvestmentHistoryController.callDeleteAPI(id);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.INVESTMENT + NotificationUtils.DELETE_FAILED);
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
        if(investmentId == null) {
            return;
        }

        Map<String, Object> data = new HashMap<>(filters);
        data.put(FilterUtils.INVESTMENT_HISTORY_INVESTMENT, investmentId);

        InvestmentHistoryResponse response = InvestmentHistoryController.callRefreshAPI(data);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.INVESTMENT + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<InvestmentHistoryDto> histories = response.getItems();
        if(histories == null) {
            histories = new ArrayList<>();
        }

        grid.setItems(histories);
    }

    @Override
    protected void editBtn() {
        openEdit(grid.getSelectedItems().iterator().next());
    }

    protected void openEdit(InvestmentHistoryDto h) {
        Map<String, Object> data = createEditMap(h);

        InvestmentHistoryAlterDlg alterDlg = new InvestmentHistoryAlterDlg(NameUtils.INVESTMENT, data, false, investmentId, paymentMethods);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        InvestmentHistoryFilterDlg filterDlg = new InvestmentHistoryFilterDlg(NameUtils.INVESTMENT, filters, paymentMethods);
        filterDlg.setOnConfirm(() -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(InvestmentHistoryDto::getDate).setHeader(NameUtils.DATE);
        grid.addColumn(InvestmentHistoryDto::getType).setHeader(NameUtils.TYPE);
        grid.addColumn(InvestmentHistoryDto::getAmount).setHeader(NameUtils.AMOUNT);
        grid.addColumn(InvestmentHistoryDto::getFee).setHeader(NameUtils.FEE);
        grid.addColumn(InvestmentHistoryDto::getShares).setHeader(NameUtils.SHARES);
        grid.addColumn(InvestmentHistoryDto::getPricePerShare).setHeader(NameUtils.PRICE_PER_SHARE);
        grid.addColumn(h -> h.getPaymentMethodDto() != null
                ? h.getPaymentMethodDto().getName()
                : "").setHeader(NameUtils.PAYMENT_METHOD);
        grid.addColumn(InvestmentHistoryDto::getNote).setHeader(NameUtils.NOTE);
    }

    protected Map<String, Object> createEditMap(InvestmentHistoryDto h) {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, h.getId());
        data.put(FilterUtils.INVESTMENT_HISTORY_DATE, h.getDate());
        data.put(FilterUtils.INVESTMENT_HISTORY_TYPE, h.getType());
        data.put(FilterUtils.INVESTMENT_HISTORY_AMOUNT, h.getAmount());
        data.put(FilterUtils.INVESTMENT_HISTORY_FEE, h.getFee());
        data.put(FilterUtils.INVESTMENT_HISTORY_SHARES, h.getShares());
        data.put(FilterUtils.INVESTMENT_HISTORY_PAYMENT_METHOD, h.getPaymentMethodDto());
        data.put(FilterUtils.INVESTMENT_HISTORY_NOTE, h.getNote());

        return data;
    }
}
