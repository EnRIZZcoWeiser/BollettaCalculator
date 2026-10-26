package org.enrycoweiser.finance.frontend.ui.dialog.filter;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardFilterDlg;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class NetWorthFilterDlg extends StandardFilterDlg {
    Map<String, Object> filters;

    protected DatePicker dateFromTxt;
    protected DatePicker dateToTxt;

    protected Runnable onConfirm;

    public NetWorthFilterDlg(String title, Map<String, Object> values) {
        super(title);

        this.filters = values;

        valueFields();
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        dateFromTxt = new DatePicker(NameUtils.DATE_FROM);
        dateToTxt = new DatePicker(NameUtils.DATE_TO);

        layout.add(
                dateFromTxt,
                dateToTxt
        );

        return layout;
    }

    @Override
    protected void confirmButtonClicked() {
        confirm();

        this.close();

        if(onConfirm != null) {
            onConfirm.run();
        }
    }

    protected void confirm() {
        filters.clear();

        LocalDate dateFrom = dateFromTxt.getValue();
        if(dateFrom != null) {
            filters.put(FilterUtils.NET_WORTH_DATE_FROM, dateFrom);
        }

        LocalDate dateTo = dateToTxt.getValue();
        if(dateTo != null) {
            filters.put(FilterUtils.NET_WORTH_DATE_TO, dateTo);
        }
    }

    protected void valueFields() {
        if(filters.get(FilterUtils.NET_WORTH_DATE_FROM) instanceof LocalDate d) {
            dateFromTxt.setValue(d);
        }

        if(filters.get(FilterUtils.NET_WORTH_DATE_TO) instanceof LocalDate d) {
            dateToTxt.setValue(d);
        }
    }
}
