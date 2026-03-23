package com.coin.simulator.infrastructure.exchange.dto;

import java.math.BigDecimal;

public record ExchangeTickerResponse(
        String market,
        BigDecimal price,
        long timestampMillis
) {}
