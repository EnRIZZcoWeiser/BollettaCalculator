package org.enrycoweiser.finance.frontend.ui.detail;

import com.vaadin.flow.component.breadcrumbs.Breadcrumbs;
import com.vaadin.flow.component.breadcrumbs.BreadcrumbsItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.GetterController;
import org.enrycoweiser.finance.frontend.controller.NetWorthController;
import org.enrycoweiser.finance.frontend.controller.NetWorthEntryController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.NetWorthEntryAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.NetWorthEntryFilterDlg;
import org.enrycoweiser.finance.frontend.ui.list.NetWorthListView;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.AssetResponse;
import org.enrycoweiser.finance.shared.api.response.NetWorthEntryResponse;
import org.enrycoweiser.finance.shared.api.response.NetWorthResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.dto.NetWorthEntryDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
    Detail of a single net worth: one row for each asset, grouped by category.
    The category rows (id == null) are built here and only show the subtotal.
 */
@Route(value = "/netWorth/detail", layout = DashboardView.class)
@PageTitle("Net Worth")
public class NetWorthDetailView extends StandardListView<NetWorthEntryDto> implements HasUrlParameter<Long> {

    protected Map<String, Object> filters;
    protected List<AssetDto> assets;
    protected Long netWorthId;

    protected BreadcrumbsItem netWorthItem;
    protected Grid.Column<NetWorthEntryDto> nameColumn;
    protected Grid.Column<NetWorthEntryDto> valueColumn;

    public NetWorthDetailView() {
        super();

        filters = new HashMap<>();
        assets = new ArrayList<>();

        addComponentAsFirst(createBreadcrumbs());

        grid.addItemDoubleClickListener(e -> {
            if(e.getItem().getId() != null) {
                openEdit(e.getItem());
            }
        });
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        netWorthId = parameter;

        loadNetWorth();
        loadAssets();

        refreshBtn();
    }

    protected Breadcrumbs createBreadcrumbs() {
        Breadcrumbs breadcrumbs = new Breadcrumbs(Breadcrumbs.Mode.MANUAL);

        netWorthItem = new BreadcrumbsItem("");

        breadcrumbs.add(
                new BreadcrumbsItem(NameUtils.NET_WORTH, NetWorthListView.class),
                netWorthItem
        );

        return breadcrumbs;
    }

    @Override
    protected Grid<NetWorthEntryDto> createGrid() {
        return new TreeGrid<>();
    }

    protected TreeGrid<NetWorthEntryDto> getTreeGrid() {
        return (TreeGrid<NetWorthEntryDto>) grid;
    }

    protected void loadNetWorth() {
        Map<String, Object> data = new HashMap<>();
        data.put(FilterUtils.ID, netWorthId);

        NetWorthResponse response = NetWorthController.callRefreshAPI(data);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.NET_WORTH + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        if(response.getItems() == null || response.getItems().isEmpty()) {
            return;
        }

        if(response.getItems().get(0).getDate() != null) {
            netWorthItem.setText(response.getItems().get(0).getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    protected void loadAssets() {
        AssetResponse response = GetterController.callGetAllAssetsAPI();
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.ASSET + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<AssetDto> items = response.getItems();
        if(items == null) {
            items = new ArrayList<>();
        }

        assets = items;
    }

    @Override
    protected void addBtn() {
        NetWorthEntryAlterDlg alterDlg = new NetWorthEntryAlterDlg(NameUtils.ASSET, null, true, netWorthId, assets);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void deleteBtn() {
        NetWorthEntryDto e = grid.getSelectedItems().iterator().next();
        if(e.getId() == null) {
            NotificationHelper.showErrorMessage(NotificationUtils.ASSET_ROW_REQUIRED);
            return;
        }

        DeleteConfirmDlg deleteDlg = new DeleteConfirmDlg();
        deleteDlg.setOnConfirm(this::delete);

        deleteDlg.open();
    }

    protected void delete() {
        Long id = grid.getSelectedItems().iterator().next().getId();

        NetWorthEntryResponse response = NetWorthEntryController.callDeleteAPI(id);
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
        if(netWorthId == null) {
            return;
        }

        Map<String, Object> data = new HashMap<>(filters);
        data.put(FilterUtils.NET_WORTH_ENTRY_NET_WORTH, netWorthId);

        NetWorthEntryResponse response = NetWorthEntryController.callRefreshAPI(data);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.NET_WORTH + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<NetWorthEntryDto> entries = response.getItems();
        if(entries == null) {
            entries = new ArrayList<>();
        }

        setTreeItems(entries);
    }

    /*
        Groups the rows by category, following the order of NET_WORTH_CATEGORIES:
        every category becomes a parent row that holds the subtotal of its assets.
     */
    protected void setTreeItems(List<NetWorthEntryDto> entries) {
        Map<String, List<NetWorthEntryDto>> children = new LinkedHashMap<>();
        for (String category : StaticUtils.NET_WORTH_CATEGORIES) {
            children.put(category, new ArrayList<>());
        }

        for (NetWorthEntryDto e : entries) {
            children.computeIfAbsent(e.getCategory(), c -> new ArrayList<>()).add(e);
        }

        List<NetWorthEntryDto> roots = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<String, List<NetWorthEntryDto>> group : children.entrySet()) {
            if(group.getValue().isEmpty()) {
                continue;
            }

            BigDecimal subtotal = BigDecimal.ZERO;
            for (NetWorthEntryDto e : group.getValue()) {
                if(e.getValue() != null) {
                    subtotal = subtotal.add(e.getValue());
                }
            }

            NetWorthEntryDto root = new NetWorthEntryDto();
            root.setCategory(group.getKey());
            root.setValue(subtotal);
            roots.add(root);

            total = total.add(subtotal);
        }

        getTreeGrid().setItems(roots, r -> r.getId() == null
                ? children.get(r.getCategory())
                : new ArrayList<>());
        getTreeGrid().expandRecursively(roots, 1);

        nameColumn.setFooter(NameUtils.NET_WORTH);
        valueColumn.setFooter(total.toPlainString());
    }

    @Override
    protected void editBtn() {
        NetWorthEntryDto e = grid.getSelectedItems().iterator().next();
        if(e.getId() == null) {
            NotificationHelper.showErrorMessage(NotificationUtils.ASSET_ROW_REQUIRED);
            return;
        }

        openEdit(e);
    }

    protected void openEdit(NetWorthEntryDto e) {
        Map<String, Object> data = createEditMap(e);

        NetWorthEntryAlterDlg alterDlg = new NetWorthEntryAlterDlg(NameUtils.ASSET, data, false, netWorthId, assets);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        NetWorthEntryFilterDlg filterDlg = new NetWorthEntryFilterDlg(NameUtils.NET_WORTH, filters);
        filterDlg.setOnConfirm(() -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        nameColumn = getTreeGrid().addHierarchyColumn(e -> e.getId() == null
                ? e.getCategory()
                : (e.getAssetDto() != null ? e.getAssetDto().getName() : ""))
                .setHeader(NameUtils.ASSET);
        valueColumn = grid.addColumn(NetWorthEntryDto::getValue).setHeader(NameUtils.VALUE);
    }

    protected Map<String, Object> createEditMap(NetWorthEntryDto e) {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, e.getId());
        data.put(FilterUtils.NET_WORTH_ENTRY_ASSET, e.getAssetDto());
        data.put(FilterUtils.NET_WORTH_ENTRY_VALUE, e.getValue());

        return data;
    }
}
