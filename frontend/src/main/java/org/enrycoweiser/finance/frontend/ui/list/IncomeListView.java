package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.IncomeAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.filter.IncomeFilterDlg;
import org.enrycoweiser.finance.shared.dto.IncomeDto;

import java.util.HashMap;
import java.util.Map;

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
