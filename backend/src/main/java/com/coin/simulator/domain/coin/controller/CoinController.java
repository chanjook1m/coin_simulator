package com.coin.simulator.domain.coin.controller;

import com.coin.simulator.domain.coin.dto.CoinResponse;
import com.coin.simulator.domain.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coins")
@RequiredArgsConstructor
public class CoinController {
    private final CoinService coinService;

    @GetMapping
    public List<CoinResponse> getCoins() {
        return coinService.getActiveCoins();
    }
}
