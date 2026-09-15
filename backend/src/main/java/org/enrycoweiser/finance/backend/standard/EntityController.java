package org.enrycoweiser.finance.backend.standard;

import org.enrycoweiser.finance.shared.standard.StandardDto;
import org.enrycoweiser.finance.shared.standard.StandardRequest;
import org.enrycoweiser.finance.shared.standard.StandardResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntityController<DTO extends StandardDto,
                                REQ extends StandardRequest,
                                RES extends StandardResponse<DTO>,
                                SER extends EntityService<DTO, REQ, RES>> {
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
