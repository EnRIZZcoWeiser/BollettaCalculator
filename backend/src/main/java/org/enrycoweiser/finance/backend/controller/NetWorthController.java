package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.NetWorthService;
import org.enrycoweiser.finance.backend.standard.EntityController;
import org.enrycoweiser.finance.shared.api.request.NetWorthRequest;
import org.enrycoweiser.finance.shared.api.response.NetWorthResponse;
import org.enrycoweiser.finance.shared.dto.NetWorthDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entity/netWorth")
public class NetWorthController extends EntityController<NetWorthDto,
                                                        NetWorthRequest,
                                                        NetWorthResponse,
                                                        NetWorthService> {
    public NetWorthController(NetWorthService service) {
        super(service);
    }
}
