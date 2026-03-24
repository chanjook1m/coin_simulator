package com.coin.simulator.infrastructure.exchange.dto;

import lombok.Builder;

@Builder
public record UpbitMarketResponse (
        String market,
        String koreanName,
        String englishName
){
}
