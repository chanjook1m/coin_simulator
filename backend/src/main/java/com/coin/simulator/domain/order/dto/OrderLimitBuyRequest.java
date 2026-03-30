package com.coin.simulator.domain.order.dto;

import java.math.BigDecimal;

public record OrderLimitBuyRequest(
        String symbol,
        BigDecimal quantity,
        BigDecimal limitPrice
) {
}
