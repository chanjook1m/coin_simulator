package com.coin.simulator.infrastructure.exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpbitTickerResponse(
        String market,
        @JsonProperty("trade_price") BigDecimal tradePrice,
        long timestamp
) {
}
