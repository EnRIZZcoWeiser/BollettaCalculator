package org.enrycoweiser.finance.frontend.ui.dialog.alter;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.PartialMatchMode;
import com.vaadin.flow.component.formlayout.FormLayout;
import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.frontend.base.StandardAlterDlg;
import org.enrycoweiser.finance.frontend.controller.InvestmentController;
import org.enrycoweiser.finance.frontend.utils.NotificationHelper;
import org.enrycoweiser.finance.shared.api.response.InvestmentResponse;
import org.enrycoweiser.finance.shared.dto.AssetDto;
import org.enrycoweiser.finance.shared.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class InvestmentAlterDlg extends StandardAlterDlg {
    protected ComboBox<AssetDto> assetCmb;
    protected Checkbox activeChk;

    protected boolean newItem;
    protected Long idDto;
    protected List<AssetDto> assets;

    protected Runnable onConfirm;

    /*
        assets: the assets that can still be chosen (not LIQUIDITY, not used by another investment).
     */
    public InvestmentAlterDlg(String title, Map<String, Object> values, boolean newItem, List<AssetDto> assets) {
        super(title);

        this.newItem = newItem;
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
        assetCmb.setPartialMatchMode(PartialMatchMode.FIRST_MATCH);
    }

    protected void setFields(Map<String, Object> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        if(values.get(FilterUtils.INVESTMENT_ASSET) instanceof AssetDto a) {
            assetCmb.setValue(findAsset(a.getId()));
        }

        if(values.get(FilterUtils.INVESTMENT_ACTIVE) instanceof Boolean b) {
            activeChk.setValue(b);
        }
    }

    protected AssetDto findAsset(Long id) {
        for (AssetDto a : assets) {
            if(Objects.equals(a.getId(), id)) {
                return a;
            }
        }

        return null;
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
        activeChk = new Checkbox(NameUtils.ACTIVE);

        layout.add(
                assetCmb,
                activeChk
        );

        return layout;
    }

    protected void confirm() {
        Map<String, Object> data = createDataMap();

        InvestmentResponse res = InvestmentController.callAlterAPI(data);
        if(res == null) {
            NotificationHelper.showErrorMessage(NameUtils.INVESTMENT + NotificationUtils.SAVE_FAILED);
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

        return null;
    }

    protected Map<String, Object> createDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put(FilterUtils.ID, idDto);
        data.put(FilterUtils.INVESTMENT_ASSET, assetCmb.getValue());
        data.put(FilterUtils.INVESTMENT_ACTIVE, activeChk.getValue());

        return data;
    }
}
