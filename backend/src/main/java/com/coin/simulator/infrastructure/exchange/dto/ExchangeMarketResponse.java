package com.coin.simulator.infrastructure.exchange.dto;

import lombok.Builder;

@Builder
public record ExchangeMarketResponse (
    String market,
    String symbol,
    String name
) {}
