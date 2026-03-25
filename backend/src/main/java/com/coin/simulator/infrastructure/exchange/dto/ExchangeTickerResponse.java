package com.coin.simulator.infrastructure.exchange.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExchangeTickerResponse(
        String market,
        BigDecimal price,
        long timestampMillis
) {}
