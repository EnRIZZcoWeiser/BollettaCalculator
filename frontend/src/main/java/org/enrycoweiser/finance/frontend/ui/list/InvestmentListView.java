package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.InvestmentController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.detail.InvestmentDetailView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.InvestmentAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.InvestmentFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.InvestmentResponse;
import org.enrycoweiser.finance.shared.dto.InvestmentDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.*;

@Route(value = "/investment", layout = DashboardView.class)
@PageTitle("Investment")
public class InvestmentListView extends StandardListView<InvestmentDto> {

    protected Map<String, Object> filters;

    public InvestmentListView() {
        super();

        filters = new HashMap<>();

        grid.addItemDoubleClickListener(e -> openDetail(e.getItem()));

        refreshBtn();
    }

    protected void openDetail(InvestmentDto i) {
        getUI().ifPresent(ui -> ui.navigate(InvestmentDetailView.class, i.getId()));
    }

    @Override
    protected void addBtn() {
        InvestmentAlterDlg alterDlg = new InvestmentAlterDlg(NameUtils.INVESTMENT, null, true);
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

        InvestmentResponse response = InvestmentController.callDeleteAPI(id);
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
        InvestmentResponse response = InvestmentController.callRefreshAPI(filters);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.INVESTMENT + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<InvestmentDto> investments = response.getItems();
        if(investments == null) {
            investments = new ArrayList<>();
        }

        grid.setItems(investments);
    }

    @Override
    protected void editBtn() {
        Map<String, Object> data = createEditMap();
        if(data == null) {
            return;
        }

        InvestmentAlterDlg alterDlg = new InvestmentAlterDlg(NameUtils.INVESTMENT, data, false);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        InvestmentFilterDlg filterDlg = new InvestmentFilterDlg(NameUtils.INVESTMENT, filters);
        filterDlg.setOnConfirm(()  -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(InvestmentDto::getName).setHeader(NameUtils.NAME);
        grid.addColumn(InvestmentDto::getCategory).setHeader(NameUtils.CATEGORY);
        grid.addColumn(InvestmentDto::getShares).setHeader(NameUtils.SHARES);
        grid.addColumn(InvestmentDto::getAveragePrice).setHeader(NameUtils.AVERAGE_PRICE);
        grid.addColumn(InvestmentDto::getInvested).setHeader(NameUtils.INVESTED);
        grid.addColumn(InvestmentDto::getFees).setHeader(NameUtils.FEES);
        grid.addColumn(InvestmentDto::getActive).setHeader(NameUtils.ACTIVE);
    }

    protected Map<String, Object> createEditMap() {
        Map<String, Object> data = new HashMap<>();

        InvestmentDto i = grid.getSelectedItems().iterator().next();
        data.put(FilterUtils.ID, i.getId());
        data.put(FilterUtils.INVESTMENT_NAME, i.getName());
        data.put(FilterUtils.INVESTMENT_CATEGORY, i.getCategory());
        data.put(FilterUtils.INVESTMENT_ACTIVE, i.getActive());

        return data;
    }
}
