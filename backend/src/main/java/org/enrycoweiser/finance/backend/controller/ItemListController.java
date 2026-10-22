package org.enrycoweiser.finance.backend.controller;

import org.enrycoweiser.finance.backend.service.ItemListService;
import org.enrycoweiser.finance.shared.api.request.ItemListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/list")
public class ItemListController {

    private final ItemListService service;

    public ItemListController(ItemListService service) {
        this.service = service;
    }

    @GetMapping("/paymentMethod")
    protected ItemListResponse getAllPaymentMethods() {
        return null;
    }
}
