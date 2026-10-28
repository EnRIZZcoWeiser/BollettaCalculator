package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.DashboardService;
import org.enrycoweiser.finance.shared.api.request.DashboardRequest;
import org.enrycoweiser.finance.shared.api.response.DashboardResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @PostMapping("/summary")
    public DashboardResponse getSummary(@RequestBody DashboardRequest request) {
        return service.getSummary(request);
    }
}
