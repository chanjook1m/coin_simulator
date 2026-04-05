package com.coin.simulator.domain.holding.dto;

import java.math.BigDecimal;

public record ActiveHoldingResponse(
        Long holdingId,
        String symbol,
        String name,
        BigDecimal quantity,
        BigDecimal avgPrice,
        BigDecimal currentPrice,
        BigDecimal valuation,
        BigDecimal floatingPnl
) {
}
