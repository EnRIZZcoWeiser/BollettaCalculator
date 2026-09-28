package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.IncomeController;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.IncomeAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.IncomeFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.request.IncomeRequest;
import org.enrycoweiser.finance.shared.api.response.IncomeResponse;
import org.enrycoweiser.finance.shared.dto.IncomeDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.*;

@Route("/income")
@PageTitle("Income")
public class IncomeListView extends StandardListView<IncomeDto,
                                                        IncomeAlterDlg,
                                                        IncomeFilterDlg> {

    protected Map<String, Object> filters;

    public IncomeListView() {
        super(IncomeDto.class, IncomeAlterDlg.class, IncomeFilterDlg.class);

        filters = new HashMap<>();

        refreshBtn();
    }

    @Override
    protected void addBtn() {
        IncomeAlterDlg alterDlg = new IncomeAlterDlg(NameUtils.INCOME, null, true);
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
        Set<IncomeDto> items = grid.getSelectedItems();
        if(items == null || items.isEmpty()) {
            NotificationHelper.showErrorMessage("Select at least 1 row!");
            return ;
        }

        if(items.size() > 1) {
            NotificationHelper.showErrorMessage("Select only 1 row!");
            return;
        }

        Long id = grid.getSelectedItems().iterator().next().getId();

        IncomeResponse response = IncomeController.callDeleteAPI(id);
        if(response == null) {
            NotificationHelper.showErrorMessage("Event Delete failed");
            return;
        } else if(response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getError());
            return;
        }

        NotificationHelper.showConfirmMessage("Success!");

        this.refreshBtn();
    }

    @Override
    protected void refreshBtn() {
        IncomeResponse response = IncomeController.callRefreshAPI(filters);
        if(response == null) {
            NotificationHelper.showErrorMessage("Income Retrieve failed");
            return;
        }

        List<IncomeDto> incomes = response.getItems();
        if(incomes == null) {
            incomes = new ArrayList<>();
        }

        grid.setItems(incomes);
    }

    @Override
    protected void editBtn() {
        Map<String, Object> data = createEditMap();
        if(data == null) {
            return;
        }

        IncomeAlterDlg alterDlg = new IncomeAlterDlg(NameUtils.INCOME, data, false);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        IncomeFilterDlg filterDlg = new IncomeFilterDlg(NameUtils.INCOME, filters);
        filterDlg.setOnConfirm(()  -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(IncomeDto::getDate).setHeader(NameUtils.DATE);
        grid.addColumn(IncomeDto::getMoney).setHeader(NameUtils.MONEY);
        grid.addColumn(IncomeDto::getCategory).setHeader(NameUtils.CATEGORY);
        grid.addColumn(i -> i.getPaymentMethodDto() != null
                                        ? i.getPaymentMethodDto().getName()
                                        : "").setHeader(NameUtils.PAYMENT_METHOD);
        grid.addColumn(IncomeDto::getNote).setHeader(NameUtils.NOTE);
    }

    protected Map<String, Object> createEditMap() {
        Map<String, Object> data = new HashMap<>();

        Set<IncomeDto> items = grid.getSelectedItems();
        if(items == null || items.isEmpty()) {
            NotificationHelper.showErrorMessage("Select at least 1 row!");
            return null;
        }

        if(items.size() > 1) {
            NotificationHelper.showErrorMessage("Select only 1 row!");
            return null;
        }

        IncomeDto i = grid.getSelectedItems().iterator().next();
        data.put(FilterUtils.INCOME_DATE, i.getDate());
        data.put(FilterUtils.INCOME_MONEY, i.getMoney());
        data.put(FilterUtils.INCOME_CATEGORY, i.getCategory());
        data.put(FilterUtils.INCOME_PAYMENT_METHOD, i.getPaymentMethodDto());
        data.put(FilterUtils.INCOME_NOTE, i.getNote());

        return data;
    }
}
