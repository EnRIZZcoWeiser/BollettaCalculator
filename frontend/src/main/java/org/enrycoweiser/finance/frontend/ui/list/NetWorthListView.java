package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.NetWorthController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.detail.NetWorthDetailView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.NetWorthAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.NetWorthFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.NetWorthResponse;
import org.enrycoweiser.finance.shared.dto.NetWorthDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.*;

@Route(value = "/netWorth", layout = DashboardView.class)
@PageTitle("Net Worth")
public class NetWorthListView extends StandardListView<NetWorthDto> {

    protected Map<String, Object> filters;

    public NetWorthListView() {
        super();

        filters = new HashMap<>();

        grid.addItemDoubleClickListener(e -> openDetail(e.getItem()));

        refreshBtn();
    }

    protected void openDetail(NetWorthDto n) {
        getUI().ifPresent(ui -> ui.navigate(NetWorthDetailView.class, n.getId()));
    }

    @Override
    protected void addBtn() {
        NetWorthAlterDlg alterDlg = new NetWorthAlterDlg(NameUtils.NET_WORTH, null, true);
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

        NetWorthResponse response = NetWorthController.callDeleteAPI(id);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.NET_WORTH + NotificationUtils.DELETE_FAILED);
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
        NetWorthResponse response = NetWorthController.callRefreshAPI(filters);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.NET_WORTH + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<NetWorthDto> netWorths = response.getItems();
        if(netWorths == null) {
            netWorths = new ArrayList<>();
        }

        grid.setItems(netWorths);
    }

    @Override
    protected void editBtn() {
        Map<String, Object> data = createEditMap();
        if(data == null) {
            return;
        }

        NetWorthAlterDlg alterDlg = new NetWorthAlterDlg(NameUtils.NET_WORTH, data, false);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        NetWorthFilterDlg filterDlg = new NetWorthFilterDlg(NameUtils.NET_WORTH, filters);
        filterDlg.setOnConfirm(() -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(NetWorthDto::getDate).setHeader(NameUtils.DATE);
        grid.addColumn(NetWorthDto::getTotal).setHeader(NameUtils.NET_WORTH);
        grid.addColumn(NetWorthDto::getInvestments).setHeader(NameUtils.INVESTMENTS);
        grid.addColumn(NetWorthDto::getSpending).setHeader(NameUtils.SPENDING);
    }

    protected Map<String, Object> createEditMap() {
        Map<String, Object> data = new HashMap<>();

        NetWorthDto n = grid.getSelectedItems().iterator().next();
        data.put(FilterUtils.ID, n.getId());
        data.put(FilterUtils.NET_WORTH_DATE, n.getDate());

        return data;
    }
}
