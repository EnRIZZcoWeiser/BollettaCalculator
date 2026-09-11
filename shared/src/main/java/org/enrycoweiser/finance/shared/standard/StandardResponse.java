package org.enrycoweiser.finance.shared.standard;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class StandardResponse<E extends StandardDto> {

    protected List<E> items;
    protected String status;
    protected String error;
    protected String errorCode;

    public StandardResponse() {
        items = null;
    }

    public StandardResponse(List<E> items) {
        this.items = Objects.requireNonNullElseGet(items, ArrayList::new);
    }

    public void createErrorResponse(String error, String errorCode){
        this.error = error;
        this.errorCode = errorCode;
        this.status = StringUtils.RESPONSE_KO;
    }

    public void createResponse() {
        this.status = StringUtils.RESPONSE_OK;
    }
}
