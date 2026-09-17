package org.enrycoweiser.finance.frontend.ui.list;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.enrycoweiser.finance.frontend.base.StandardListView;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.ExpenseAlterDlg;
import org.enrycoweiser.finance.frontend.ui.dialog.alter.ExpenseFilterDlg;
import org.enrycoweiser.finance.shared.dto.ExpenseDto;

@Route("/expense")
@PageTitle("Expense")
public class ExpenseListView extends StandardListView<ExpenseDto,
                                                        ExpenseAlterDlg,
                                                        ExpenseFilterDlg> {
    public ExpenseListView() {
        super(ExpenseDto.class, ExpenseAlterDlg.class, ExpenseFilterDlg.class);
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
