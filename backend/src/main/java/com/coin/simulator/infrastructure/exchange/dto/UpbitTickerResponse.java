package com.coin.simulator.infrastructure.exchange.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpbitTickerResponse (
        String market,
        BigDecimal tradePrice,
        long timestamp
){ }
