package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.NetWorthEntryService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.NetWorthEntryRequest;
import org.enrycoweiser.finance.shared.api.response.NetWorthEntryResponse;
import org.enrycoweiser.finance.shared.dto.NetWorthEntryDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/netWorthEntry")
public class NetWorthEntryController extends EntityController<NetWorthEntryDto,
                                                        NetWorthEntryRequest,
                                                        NetWorthEntryResponse,
                                                        NetWorthEntryService> {
    public NetWorthEntryController(NetWorthEntryService service) {
        super(service);
    }
}
