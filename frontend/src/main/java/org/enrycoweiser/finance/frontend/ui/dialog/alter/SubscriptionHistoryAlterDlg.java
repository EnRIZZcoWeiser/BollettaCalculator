package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.PartialMatchMode;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SubscriptionHistoryAlterDlg extends StandardAlterDlg {
    protected DatePicker startDateTxt;
    protected DatePicker endDateTxt;
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
        setFields(values);
        setId(values);

        startDateTxt.addValueChangeListener(e -> proposeEndDate());
    }

    protected void valueComboBoxes() {
        valuePaymentMethodCombo();
    }

    protected void valuePaymentMethodCombo() {
        paymentMethodCmb.setItems(
                paymentMethods
        );
        paymentMethodCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);
    }

    /*
        Every period is annual: the end date is proposed as start + 1 year - 1 day,
        only while it is still empty, so a date typed by hand is never overwritten.
     */
    protected void proposeEndDate() {
        if(startDateTxt.getValue() == null || endDateTxt.getValue() != null) {
            return;
        }

        endDateTxt.setValue(startDateTxt.getValue().plusYears(1).minusDays(1));
    }

    protected void setFields(Map<String, Object> values) {
        if(values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.SUBSCRIPTION_HISTORY_START_DATE) instanceof LocalDate d) {
            startDateTxt.setValue(d);
        }

        if(values.get(FilterUtils.SUBSCRIPTION_HISTORY_END_DATE) instanceof LocalDate d) {
            endDateTxt.setValue(d);
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

        startDateTxt = new DatePicker(NameUtils.START_DATE);
        endDateTxt = new DatePicker(NameUtils.END_DATE);
        priceTxt = new BigDecimalField(NameUtils.PRICE);
        paymentMethodCmb = new ComboBox<>(NameUtils.PAYMENT_METHOD);

        layout.add(
                startDateTxt,
                endDateTxt,
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
        if(startDateTxt.getValue() == null) {
            return NotificationUtils.START_DATE_REQUIRED;
        }

        if(endDateTxt.getValue() == null) {
            return NotificationUtils.END_DATE_REQUIRED;
        }

        if(endDateTxt.getValue().isBefore(startDateTxt.getValue())) {
            return NotificationUtils.END_DATE_BEFORE_START;
        }

        if(priceTxt.getValue() == null || priceTxt.getValue().signum() <= 0) {
            return NotificationUtils.PRICE_REQUIRED;
        }

        if(paymentMethodCmb.getValue() == null) {
            return NotificationUtils.PAYMENT_METHOD_REQUIRED;
        }

        return null;
    }

    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_SUBSCRIPTION, subscriptionId);
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_START_DATE, startDateTxt.getValue());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_END_DATE, endDateTxt.getValue());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_PRICE, priceTxt.getValue());
        data.put(FilterUtils.SUBSCRIPTION_HISTORY_PAYMENT_METHOD, paymentMethodCmb.getValue());

        return data;
    }
}
