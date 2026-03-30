package com.coin.simulator.domain.coin.service;

import com.coin.simulator.domain.coin.dto.CoinResponse;
import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.coin.entity.CoinStatus;
import com.coin.simulator.domain.coin.repository.CoinRepository;
import com.coin.simulator.infrastructure.exchange.ExchangeClient;
import com.coin.simulator.infrastructure.exchange.dto.ExchangeMarketResponse;
import com.coin.simulator.infrastructure.exchange.exception.MarketDataEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinService {
    private final CoinRepository coinRepository;
    private final ExchangeClient exchangeClient;


    public List<CoinResponse> getActiveCoins() {
        List<Coin> coins = coinRepository.findAllByStatus(CoinStatus.ACTIVE);
        if (!coins.isEmpty()) {
            return coins.stream().map(CoinResponse::from).toList();
        }

        List<ExchangeMarketResponse> markets;

        markets = exchangeClient.getMarkets();

        if (markets.isEmpty()) {
            log.warn("거래소에서 가져온 마켓 목록 비어 있음");
            throw new MarketDataEmptyException();
        }

        List<Coin> newCoins = markets.stream()
                .map(m -> Coin.builder()
                        .symbol(m.symbol())
                        .name(m.name())
                        .market(m.market())
                        .build())
                .toList();

        return saveAllCoinListAndReturnResponses(newCoins);
    }

    @Transactional
    public List<CoinResponse> saveAllCoinListAndReturnResponses(List<Coin> newCoins) {
        List<Coin> savedCoins = coinRepository.saveAll(newCoins);
        return savedCoins.stream().map(CoinResponse::from).toList();
    }
}
