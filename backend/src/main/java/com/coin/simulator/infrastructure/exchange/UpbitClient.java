package com.coin.simulator.infrastructure.exchange;

import com.coin.simulator.infrastructure.exchange.dto.ExchangeMarketResponse;
import com.coin.simulator.infrastructure.exchange.dto.ExchangeTickerResponse;
import com.coin.simulator.infrastructure.exchange.dto.UpbitMarketResponse;
import com.coin.simulator.infrastructure.exchange.dto.UpbitTickerResponse;
import com.coin.simulator.infrastructure.exchange.exception.ExchangeConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpbitClient implements ExchangeClient {
    private static final String KRW_PREFIX = "KRW-";
    private static final String GET_ALL_COIN_LIST = "/v1/market/all?isDetails=true";
    private final RestClient upbitRestClient;

    @Override
    public List<ExchangeMarketResponse> getMarkets() {
        try {
            List<UpbitMarketResponse> res = upbitRestClient.get()
                    .uri(GET_ALL_COIN_LIST)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<UpbitMarketResponse>>() {
                    });

            if (res == null) {
                log.warn("[Upbit] /market/all 응답이 null");
                return List.of();
            }

            return res.stream()
                    .filter(m -> m.market().startsWith(KRW_PREFIX))
                    .map(m -> ExchangeMarketResponse.builder()
                            .market(m.market())
                            .symbol(extractSymbol(m.market()))
                            .name(m.koreanName())
                            .build())
                    .toList();

        } catch (RestClientException e) {
            log.error("[Upbit] getMarkets 호출 실패", e);
            throw new ExchangeConnectionException(e);
        }
    }

    @Override
    public List<ExchangeTickerResponse> getTicker(List<String> markets) {
        String marketsParam = String.join(",", markets);

        try {
            List<UpbitTickerResponse> res = upbitRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/ticker")
                            .queryParam("markets", marketsParam)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<UpbitTickerResponse>>() {
                    });

            if (res == null || res.isEmpty()) {
                log.warn("[Upbit] /ticker 응답이 비어 있음", marketsParam);
                return List.of();
            }

            return res.stream()
                    .map(t -> ExchangeTickerResponse.builder()
                            .market(t.market())
                            .price(t.tradePrice())
                            .timestampMillis(t.timestamp())
                            .build())
                    .toList();

        } catch (RestClientException e) {
            log.error("[Upbit] getTickers 호출 실패", marketsParam, e);
            throw new ExchangeConnectionException(e);
        }

    }

    private String extractSymbol(String market) {
        return market.substring(KRW_PREFIX.length());
    }
}
