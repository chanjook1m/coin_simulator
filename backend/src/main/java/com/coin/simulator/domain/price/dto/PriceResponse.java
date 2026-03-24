package com.coin.simulator.domain.price.dto;

import com.coin.simulator.domain.price.entity.Price;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PriceResponse(
        String symbol,
        BigDecimal price,
        LocalDateTime timestamp
) {
    public static PriceResponse from(Price price) {
        return PriceResponse.builder()
                .symbol(price.getCoin().getSymbol())
                .price(price.getPrice())
                .timestamp(price.getTimestamp())
                .build();
    }
}
