package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.PartialMatchMode;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;
import org.enrycoweiser.finance.frontend.controller.SubscriptionHistoryController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.SubscriptionHistoryResponse;
import org.enrycoweiser.finance.shared.dto.PaymentMethodDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class SubscriptionHistoryAlterDlg extends StandardAlterDlg {
    protected ComboBox<Integer> startYearCmb;
    protected ComboBox<Integer> startMonthCmb;
    protected ComboBox<Integer> endYearCmb;
    protected ComboBox<Integer> endMonthCmb;
    protected BigDecimalField priceTxt;
    protected ComboBox<PaymentMethodDto> paymentMethodCmb;

    protected boolean newItem;
    protected Long idDto;
    protected Long subscriptionId;
    protected List<PaymentMethodDto> paymentMethods;

    protected Runnable onConfirm;

    public SubscriptionHistoryAlterDlg(String title, Map<String, Object> values, boolean newItem, Long subscriptionId, List<PaymentMethodDto> paymentMethods) {
        super(title);

        this.newItem = newItem;
        this.subscriptionId = subscriptionId;
        this.paymentMethods = paymentMethods;

        valueComboBoxes();
        setDefaults();
        setFields(values);
        setId(values);

        startYearCmb.addValueChangeListener(e -> startChanged());
        startMonthCmb.addValueChangeListener(e -> startChanged());
        endYearCmb.addValueChangeListener(e -> showMonths());
        endMonthCmb.addValueChangeListener(e -> showMonths());
        showMonths();
    }

    protected void valueComboBoxes() {
        valueYearCombos();
        valueMonthCombos();
        valuePaymentMethodCombo();
    }

    protected void valueYearCombos() {
        startYearCmb.setItems(StaticUtils.YEARS);
        endYearCmb.setItems(StaticUtils.YEARS);
    }

    protected void valueMonthCombos() {
        startMonthCmb.setItems(StaticUtils.MONTHS);
        startMonthCmb.setItemLabelGenerator(this::monthName);
        startMonthCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);

        endMonthCmb.setItems(StaticUtils.MONTHS);
        endMonthCmb.setItemLabelGenerator(this::monthName);
        endMonthCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);
    }

    protected void valuePaymentMethodCombo() {
        paymentMethodCmb.setItems(
                paymentMethods
        );
        paymentMethodCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);
    }

    protected String monthName(Integer month) {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    /*
        A new period starts from the current month and lasts one year.
     */
    protected void setDefaults() {
        if(!newItem) {
            return;
        }

        LocalDate today = LocalDate.now();
        startYearCmb.setValue(today.getYear());
        startMonthCmb.setValue(today.getMonthValue());

        proposeEnd();
    }

    protected void startChanged() {
        proposeEnd();
        showMonths();
    }

    /*
        The end month is the last month covered, included.
        While it is empty it is proposed one year after the start (start + 11 months).
     */
    protected void proposeEnd() {
        YearMonth start = getStart();
        if(start == null || endYearCmb.getValue() != null || endMonthCmb.getValue() != null) {
            return;
        }

        setEnd(start.plusMonths(11));
    }

    protected void showMonths() {
        YearMonth start = getStart();
        YearMonth end = getEnd();
        if(start == null || end == null || end.isBefore(start)) {
            endMonthCmb.setHelperText(null);
            return;
        }

        long months = ChronoUnit.MONTHS.between(start, end) + 1;
        endMonthCmb.setHelperText(months + " " + NameUtils.MONTHS.toLowerCase());
    }

    /* null until both year and month are chosen */
    protected YearMonth getStart() {
        if(startYearCmb.getValue() == null || startMonthCmb.getValue() == null) {
            return null;
        }

        return YearMonth.of(startYearCmb.getValue(), startMonthCmb.getValue());
    }

    protected YearMonth getEnd() {
        if(endYearCmb.getValue() == null || endMonthCmb.getValue() == null) {
            return null;
        }

        return YearMonth.of(endYearCmb.getValue(), endMonthCmb.getValue());
    }

    protected void setStart(YearMonth month) {
        try {
            startYearCmb.setValue(month.getYear());
            startMonthCmb.setValue(month.getMonthValue());
        } catch (IllegalArgumentException iae) {
            startYearCmb.setValue(null);
            startMonthCmb.setValue(null);
        }
    }

    protected void setEnd(YearMonth month) {
        try {
            endYearCmb.setValue(month.getYear());
            endMonthCmb.setValue(month.getMonthValue());
        } catch (IllegalArgumentException iae) {
            endYearCmb.setValue(null);
            endMonthCmb.setValue(null);
        }
    }

    protected void setFields(Map<String, Object> values) {
        if(values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.SUBSCRIPTION_HISTORY_START_DATE) instanceof LocalDate d) {
            setStart(YearMonth.from(d));
        }

        if(values.get(FilterUtils.SUBSCRIPTION_HISTORY_END_DATE) instanceof LocalDate d) {
            setEnd(YearMonth.from(d));
        }

        if(values.get(FilterUtils.SUBSCRIPTION_HISTORY_PRICE) instanceof BigDecimal b) {
            priceTxt.setValue(b);
        }

        if(values.get(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD) instanceof PaymentMethodDto p) {
            try {
                paymentMethodCmb.setValue(p);
            } catch (IllegalArgumentException iae) {
                paymentMethodCmb.setValue(null);
            }
        }
    }

    protected void setId(Map<String, Object> values) {
        if(values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.ID) instanceof Long l) {
            this.idDto = l;
        }
    }

    @Override
    protected FormLayout createLayout() {
        FormLayout layout = new FormLayout();

        startYearCmb = new ComboBox<>(NameUtils.START_YEAR);
        startMonthCmb = new ComboBox<>(NameUtils.START_MONTH);
        endYearCmb = new ComboBox<>(NameUtils.END_YEAR);
        endMonthCmb = new ComboBox<>(NameUtils.END_MONTH);
        priceTxt = new BigDecimalField(NameUtils.PRICE);
        paymentMethodCmb = new ComboBox<>(NameUtils.PAYMENT_METHOD);

        layout.add(
                startYearCmb,
                startMonthCmb,
                endYearCmb,
                endMonthCmb,
                priceTxt,
                paymentMethodCmb
        );

        return layout;
    }

    @Override
    protected void confirm() {
        Map<String, Object> data = createDataMap();

        SubscriptionHistoryResponse res = SubscriptionHistoryController.callAlterAPI(data);
        if(res == null) {
            NotificationHelper.showErrorMessage(NameUtils.SUBSCRIPTION + NotificationUtils.SAVE_FAILED);
            return;
        } else if(res.getStatus() != null && res.getStatus().equals(StringUtils.RESPONSE_KO)) {
            NotificationHelper.showErrorMessage(res.getErrorCode() + " - " + res.getError());
            return;
        }

        NotificationHelper.showConfirmMessage(NotificationUtils.SUCCESS);

        this.close();

        if(onConfirm != null) {
            onConfirm.run();
        }
    }

    @Override
    protected String checkData() {
        if(getStart() == null) {
            return NotificationUtils.START_MONTH_REQUIRED;
        }

        if(getEnd() == null) {
            return NotificationUtils.END_MONTH_REQUIRED;
        }

        if(getEnd().isBefore(getStart())) {
            return NotificationUtils.END_MONTH_BEFORE_START;
        }

        if(priceTxt.getValue() == null || priceTxt.getValue().signum() <= 0) {
            return NotificationUtils.PRICE_REQUIRED;
        }

        if(paymentMethodCmb.getValue() == null) {
            return NotificationUtils.PAYMENT_METHOD_REQUIRED;
        }

        return null;
    }

    /*
        The month is sent as a date: first day of the start month, last day of the end month.
     */
    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_SUBSCRIPTION, subscriptionId);
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_START_DATE, getStart().atDay(1));
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_END_DATE, getEnd().atEndOfMonth());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_PRICE, priceTxt.getValue());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD, paymentMethodCmb.getValue());

        return data;
    }
}
