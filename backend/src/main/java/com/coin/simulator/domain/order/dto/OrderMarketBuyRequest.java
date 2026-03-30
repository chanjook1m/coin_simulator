package com.coin.simulator.domain.order.dto;

import java.math.BigDecimal;

public record OrderMarketBuyRequest(
        String symbol,
        BigDecimal quantity
) {
}
