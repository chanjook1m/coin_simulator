package com.coin.simulator.infrastructure.exchange.dto;

public record ExchangeMarketResponse (
    String market,
    String symbol,
    String name
) {}
