package com.coin.simulator.infrastructure.exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UpbitMarketResponse(
        String market,
        @JsonProperty("korean_name") String koreanName,
        @JsonProperty("english_name") String englishName
) {
}
