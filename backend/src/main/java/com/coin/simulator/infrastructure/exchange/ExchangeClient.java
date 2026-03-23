package com.coin.simulator.infrastructure.exchange;

import com.coin.simulator.infrastructure.exchange.dto.ExchangeMarketResponse;
import com.coin.simulator.infrastructure.exchange.dto.ExchangeTickerResponse;

import java.util.List;

public interface ExchangeClient {
    List<ExchangeMarketResponse> getMarkets();
    List<ExchangeTickerResponse> getTicker(List<String> markets);
}
