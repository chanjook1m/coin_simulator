package com.coin.simulator.domain.holding.controller;

import com.coin.simulator.domain.holding.dto.ActiveHoldingResponse;
import com.coin.simulator.domain.holding.service.HoldingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/holdings")
@RequiredArgsConstructor
public class HoldingController {

    private final HoldingService holdingService;

    @GetMapping("/active")
    public List<ActiveHoldingResponse> getActiveHoldings(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return holdingService.getActiveHoldings(userId);
    }
}
