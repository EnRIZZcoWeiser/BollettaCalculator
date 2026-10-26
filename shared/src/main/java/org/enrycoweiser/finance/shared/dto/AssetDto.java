package org.enrycoweiser.finance.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.enrycoweiser.finance.shared.standard.StandardDto;

@Getter
@Setter
public class AssetDto extends StandardDto {
    protected String name;
    protected String category;
    protected Boolean active;
    protected Long userId;

    @Override
    public String toString() {
        return this.name;
    }
}
