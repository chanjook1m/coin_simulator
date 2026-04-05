package com.coin.simulator.domain.ledger.controller;

import com.coin.simulator.domain.ledger.dto.LedgerHistoryResponse;
import com.coin.simulator.domain.ledger.service.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping("/history")
    public Page<LedgerHistoryResponse> getWalletHistory(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ledgerService.getHistory(userId, page, size);
    }
}
