package com.coin.simulator.domain.ledger.dto;

import com.coin.simulator.domain.ledger.entity.Ledger;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LedgerHistoryResponse(
        Long id,
        String asset,
        BigDecimal changeAmount,
        String reason,
        LocalDateTime createdAt
) {
    public static LedgerHistoryResponse from(Ledger ledger) {
        return new LedgerHistoryResponse(
                ledger.getId(),
                ledger.getAsset(),
                ledger.getChangeAmount(),
                ledger.getReason(),
                ledger.getCreatedAt()
        );
    }
}
