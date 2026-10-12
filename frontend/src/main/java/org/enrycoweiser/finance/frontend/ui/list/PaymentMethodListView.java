package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.PaymentMethodAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.PaymentMethodFilterDlg;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;

@Route("/paymentMethod")
@PageTitle("Payment Method")
public class PaymentMethodListView extends StandardListView<PaymentMethodDto,
                                                            PaymentMethodAlterDlg,
                                                            PaymentMethodFilterDlg> {

    public PaymentMethodListView(Class<PaymentMethodDto> entityClass, Class<PaymentMethodAlterDlg> alterDlgClass, Class<PaymentMethodFilterDlg> filterDlgClass) {
        super(entityClass, alterDlgClass, filterDlgClass);
    }

    @Override
    protected void addBtn() {

    }

    @Override
    protected void deleteBtn() {

    }

    @Override
    protected void refreshBtn() {

    }

    @Override
    protected void editBtn() {

    }

    @Override
    protected void filterBtn() {

    }

    @Override
    protected void configureColumns() {

    }
}
