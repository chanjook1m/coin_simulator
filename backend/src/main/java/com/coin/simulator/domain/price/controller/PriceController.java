package com.coin.simulator.domain.price.controller;

import com.coin.simulator.domain.price.dto.PriceResponse;
import com.coin.simulator.domain.price.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @GetMapping("/{symbol}")
    public PriceResponse getPrice(@PathVariable String symbol) {
        return priceService.getLatestPrice(symbol);
    }
}
