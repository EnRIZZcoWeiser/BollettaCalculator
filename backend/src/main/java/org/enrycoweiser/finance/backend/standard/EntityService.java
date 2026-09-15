package org.enrycoweiser.finance.backend.standard;

import jakarta.transaction.Transactional;
import org.enrycoweiser.finance.shared.standard.StandardDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import org.springframework.stereotype.Service;

public interface EntityService<DTO extends StandardDto,
                                REQ extends StandardRequest,
                                RES extends StandardResponse<DTO>> {
    public abstract RES save(REQ request);

    public abstract RES delete(REQ request);

    public abstract RES retrieve(REQ request);
}
