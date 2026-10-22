package org.enrycoweiser.finance.shared.api.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemListResponse {
    protected List<Object> items;
}
