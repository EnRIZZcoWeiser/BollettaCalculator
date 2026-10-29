package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.GetterController;
import org.enrycoweiser.finance.frontend.controller.InvestmentController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.detail.InvestmentDetailView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.InvestmentAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.InvestmentFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.AssetResponse;
import org.enrycoweiser.finance.shared.api.response.InvestmentResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.dto.InvestmentDto;
import org.enrycoweiser.finance.shared.utils.*;

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
        InvestmentAlterDlg alterDlg = new InvestmentAlterDlg(NameUtils.INVESTMENT, null, true, loadAvailableAssets(null));
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    /*
        Assets that can be linked to an investment: not LIQUIDITY and not already used by another investment.
        The asset of the investment being edited (currentAsset) stays available.
        Loaded every time a dialog opens, so a new asset or investment is always taken into account.
     */
    protected List<AssetDto> loadAvailableAssets(AssetDto currentAsset) {
        AssetResponse assetResponse = GetterController.callGetAllAssetsAPI();
        if(assetResponse == null) {
            NotificationHelper.showErrorMessage(NameUtils.ASSET + NotificationUtils.RETRIEVE_FAILED);
            return new ArrayList<>();
        } else if(assetResponse.getStatus() != null && assetResponse.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(assetResponse.getErrorCode() + " - " + assetResponse.getError());
            return new ArrayList<>();
        }

        // all the investments, not only the filtered ones in the grid
        InvestmentResponse investmentResponse = InvestmentController.callRefreshAPI(new HashMap<>());
        if(investmentResponse == null) {
            NotificationHelper.showErrorMessage(NameUtils.INVESTMENT + NotificationUtils.RETRIEVE_FAILED);
            return new ArrayList<>();
        } else if(investmentResponse.getStatus() != null && investmentResponse.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(investmentResponse.getErrorCode() + " - " + investmentResponse.getError());
            return new ArrayList<>();
        }

        Set<Long> usedAssets = new HashSet<>();
        if(investmentResponse.getItems() != null) {
            for (InvestmentDto i : investmentResponse.getItems()) {
                if(i.getAssetDto() != null) {
                    usedAssets.add(i.getAssetDto().getId());
                }
            }
        }
        if(currentAsset != null) {
            usedAssets.remove(currentAsset.getId());
        }

        List<AssetDto> available = new ArrayList<>();
        if(assetResponse.getItems() != null) {
            for (AssetDto a : assetResponse.getItems()) {
                if(StaticUtils.NET_WORTH_LIQUIDITY.equals(a.getCategory()) || usedAssets.contains(a.getId())) {
                    continue;
                }

                available.add(a);
            }
        }

        return available;
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

        InvestmentDto i = grid.getSelectedItems().iterator().next();
        InvestmentAlterDlg alterDlg = new InvestmentAlterDlg(NameUtils.INVESTMENT, data, false, loadAvailableAssets(i.getAssetDto()));
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
        data.put(FilterUtils.INVESTMENT_ASSET, i.getAssetDto());
        data.put(FilterUtils.INVESTMENT_ACTIVE, i.getActive());

        return data;
    }
}
