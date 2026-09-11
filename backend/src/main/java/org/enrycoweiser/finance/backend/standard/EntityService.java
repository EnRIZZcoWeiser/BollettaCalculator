package org.enrycoweiser.finance.backend.standard;

import jakarta.transaction.Transactional;
import org.enrycoweiser.finance.shared.standard.StandardRequest;
import org.enrycoweiser.finance.shared.standard.StandardResponse;

public interface EntityService<REQ extends StandardRequest, RES extends StandardResponse> {
    @Transactional
    public abstract RES save(REQ request);

    @Transactional
    public abstract RES delete(REQ request);

    @Transactional
    public abstract RES retrieve(REQ request);
}
