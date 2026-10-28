package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.DeleteConfirmDlg;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.controller.GetterController;
import org.enrycoweiser.finance.frontend.controller.TransferController;
import org.enrycoweiser.finance.frontend.ui.DashboardView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.TransferAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.TransferFilterDlg;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.TransferResponse;
import org.enrycoweiser.finance.shared.api.response.PaymentMethodResponse;
import org.enrycoweiser.finance.shared.dto.TransferDto;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.*;

@Route(value = "/transfer", layout = DashboardView.class)
@PageTitle("Transfer")
public class TransferListView extends StandardListView<TransferDto> {

    protected Map<String, Object> filters;
    protected List<PaymentMethodDto> paymentMethods;

    public TransferListView() {
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
        TransferAlterDlg alterDlg = new TransferAlterDlg(NameUtils.TRANSFER, null, true, paymentMethods);
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

        TransferResponse response = TransferController.callDeleteAPI(id);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.TRANSFER + NotificationUtils.DELETE_FAILED);
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
        TransferResponse response = TransferController.callRefreshAPI(filters);
        if(response == null) {
            NotificationHelper.showErrorMessage(NameUtils.TRANSFER + NotificationUtils.RETRIEVE_FAILED);
            return;
        } else if(response.getStatus() != null && response.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(response.getErrorCode() + " - " + response.getError());
            return;
        }

        List<TransferDto> transfers = response.getItems();
        if(transfers == null) {
            transfers = new ArrayList<>();
        }

        grid.setItems(transfers);
    }

    @Override
    protected void editBtn() {
        Map<String, Object> data = createEditMap();
        if(data == null) {
            return;
        }

        TransferAlterDlg alterDlg = new TransferAlterDlg(NameUtils.TRANSFER, data, false, paymentMethods);
        alterDlg.setOnConfirm(this::refreshBtn);

        alterDlg.open();
    }

    @Override
    protected void filterBtn() {
        TransferFilterDlg filterDlg = new TransferFilterDlg(NameUtils.TRANSFER, filters, paymentMethods);
        filterDlg.setOnConfirm(()  -> {
            filters = filterDlg.getFilters();
            refreshBtn();
        });
        filterDlg.open();
    }

    @Override
    protected void configureColumns() {
        grid.addColumn(TransferDto::getDate).setHeader(NameUtils.DATE);
        grid.addColumn(t -> t.getFromPaymentMethodDto() != null
                                        ? t.getFromPaymentMethodDto().getName()
                                        : "").setHeader(NameUtils.FROM);
        grid.addColumn(t -> t.getToPaymentMethodDto() != null
                                        ? t.getToPaymentMethodDto().getName()
                                        : "").setHeader(NameUtils.TO);
        grid.addColumn(TransferDto::getMoney).setHeader(NameUtils.MONEY);
        grid.addColumn(TransferDto::getNote).setHeader(NameUtils.NOTE);
    }

    protected Map<String, Object> createEditMap() {
        Map<String, Object> data = new HashMap<>();

        TransferDto t = grid.getSelectedItems().iterator().next();
        data.put(FilterUtils.ID, t.getId());
        data.put(FilterUtils.TRANSFER_DATE, t.getDate());
        data.put(FilterUtils.TRANSFER_FROM, t.getFromPaymentMethodDto());
        data.put(FilterUtils.TRANSFER_TO, t.getToPaymentMethodDto());
        data.put(FilterUtils.TRANSFER_MONEY, t.getMoney());
        data.put(FilterUtils.TRANSFER_NOTE, t.getNote());

        return data;
    }
}
