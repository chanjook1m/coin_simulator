package com.coin.simulator.domain.coin.dto;

import com.coin.simulator.domain.coin.entity.Coin;
import lombok.Builder;

@Builder
public record CoinResponse(
        String symbol,
        String name,
        String market
) {
    public static CoinResponse from(Coin coin) {
        return CoinResponse.builder()
                .symbol(coin.getSymbol())
                .name(coin.getName())
                .market(coin.getMarket())
                .build();
    }
}
