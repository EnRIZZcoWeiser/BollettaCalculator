package org.enrycoweiser.finance.backend.standard;

import org.enrycoweiser.finance.shared.standard.StandardRequest;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntityController<SER extends EntityService<REQ, RES>, REQ extends StandardRequest, RES extends StandardResponse> {
    private final SER service;

    public EntityController(SER service) {
        this.service = service;
    }

    @PostMapping("/save")
    public RES create(@RequestBody REQ request) {
        return service.save(request);
    }

    @PostMapping("/delete")
    public RES delete(@RequestBody REQ request) {
        return service.delete(request);
    }

    @PostMapping("/filter")
    public RES retrieve(@RequestBody REQ request) {
        return service.retrieve(request);
    }
}
