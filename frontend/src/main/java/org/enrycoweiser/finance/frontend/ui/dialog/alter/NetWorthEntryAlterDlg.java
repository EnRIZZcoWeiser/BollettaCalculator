package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;
import org.enrycoweiser.finance.frontend.controller.NetWorthEntryController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.NetWorthEntryResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.utils.FilterUtils;
import org.enrycoweiser.finance.shared.utils.NameUtils;
import org.enrycoweiser.finance.shared.utils.NotificationUtils;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class NetWorthEntryAlterDlg extends StandardAlterDlg {
    protected ComboBox<AssetDto> assetCmb;
    protected BigDecimalField valueTxt;

    protected boolean newItem;
    protected Long idDto;
    protected Long netWorthId;
    protected List<AssetDto> assets;

    protected Runnable onConfirm;

    public NetWorthEntryAlterDlg(String title, Map<String, Object> values, boolean newItem, Long netWorthId, List<AssetDto> assets) {
        super(title);

        this.newItem = newItem;
        this.netWorthId = netWorthId;
        this.assets = assets;

        valueComboBoxes();
        setFields(values);
        setId(values);
    }

    protected void valueComboBoxes() {
        valueAssetCombo();
    }

    protected void valueAssetCombo() {
        assetCmb.setItems(
                assets
        );

        // the asset of an existing row can't be changed, only its value
        assetCmb.setReadOnly(!newItem);
    }

    protected void setFields(Map<String, Object> values) {
        if(values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.NET_WORTH_ENTRY_ASSET) instanceof AssetDto a) {
            try {
                assetCmb.setValue(a);
            } catch (IllegalArgumentException iae) {
                assetCmb.setValue(null);
            }
        }

        if(values.get(FilterUtils.NET_WORTH_ENTRY_VALUE) instanceof BigDecimal b) {
            valueTxt.setValue(b);
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

        assetCmb = new ComboBox<>(NameUtils.ASSET);
        valueTxt = new BigDecimalField(NameUtils.VALUE);

        layout.add(
                assetCmb,
                valueTxt
        );

        return layout;
    }

    @Override
    protected void confirm() {
        Map<String, Object> data = createDataMap();

        NetWorthEntryResponse res = NetWorthEntryController.callAlterAPI(data);
        if(res == null) {
            NotificationHelper.showErrorMessage(NameUtils.ASSET + NotificationUtils.SAVE_FAILED);
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
        if(assetCmb.getValue() == null) {
            return NotificationUtils.ASSET_REQUIRED;
        }

        if(valueTxt.getValue() == null) {
            return NotificationUtils.VALUE_REQUIRED;
        }

        return null;
    }

    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);
        data.put(FilterUtils.NET_WORTH_ENTRY_NET_WORTH, netWorthId);
        data.put(FilterUtils.NET_WORTH_ENTRY_ASSET, assetCmb.getValue());
        data.put(FilterUtils.NET_WORTH_ENTRY_VALUE, valueTxt.getValue());

        return data;
    }
}
