package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.AssetController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.AssetAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.AssetFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.AssetResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.*;

@Route(value = "/asset", layout = DashboardView.class)
@PageTitle("Asset")
public class AssetListView extends StandardListView<AssetDto> {

    protected Map<String, Object> filters;

    public AssetListView() {
        super();

        filters = new HashMap<>();

        refreshBtn();
    }

    @Override
    protected void addBtn() {
        AssetAlterDlg alterDlg = new AssetAlterDlg(NameUtils.ASSET, null, true);
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

        AssetResponse response = AssetController.callDeleteAPI(id);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.ASSET + NotificationUtils.DELETE_FAILED);
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
        AssetResponse response = AssetController.callRefreshAPI(filters);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.ASSET + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<AssetDto> assets = response.getItems();
        if(assets == null) {
            assets = new ArrayList<>();
        }

        grid.setItems(assets);
    }

    @Override
    protected void editBtn() {
        Map<String, Object> data = createEditMap();
        if(data == null) {
            return;
        }

        AssetAlterDlg alterDlg = new AssetAlterDlg(NameUtils.ASSET, data, false);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        AssetFilterDlg filterDlg = new AssetFilterDlg(NameUtils.ASSET, filters);
        filterDlg.setOnConfirm(()  -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(AssetDto::getName).setHeader(NameUtils.NAME);
        grid.addColumn(AssetDto::getCategory).setHeader(NameUtils.CATEGORY);
        grid.addColumn(AssetDto::getActive).setHeader(NameUtils.ACTIVE);
    }

    protected Map<String, Object> createEditMap() {
        Map<String, Object> data = new HashMap<>();

        AssetDto a = grid.getSelectedItems().iterator().next();
        data.put(FilterUtils.ID, a.getId());
        data.put(FilterUtils.ASSET_NAME, a.getName());
        data.put(FilterUtils.ASSET_CATEGORY, a.getCategory());
        data.put(FilterUtils.ASSET_ACTIVE, a.getActive());

        return data;
    }
}
