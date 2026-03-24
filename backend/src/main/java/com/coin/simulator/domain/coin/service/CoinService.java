package com.coin.simulator.domain.coin.service;

import com.coin.simulator.common.exception.NotFoundException;
import com.coin.simulator.domain.coin.dto.CoinResponse;
import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.coin.entity.CoinStatus;
import com.coin.simulator.domain.coin.repository.CoinRepository;
import com.coin.simulator.infrastructure.exchange.ExchangeClient;
import com.coin.simulator.infrastructure.exchange.ExternalExchangeException;
import com.coin.simulator.infrastructure.exchange.dto.ExchangeMarketResponse;
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

    @Transactional(readOnly = true)
    public List<CoinResponse> getActiveCoins() {
        List<Coin> coins = coinRepository.findAllByStatus(CoinStatus.ACTIVE);
        if (!coins.isEmpty()) {
            return coins.stream().map(CoinResponse::from).toList();
        }

        List<ExchangeMarketResponse> markets;
        try {
            markets = exchangeClient.getMarkets();
        } catch (ExternalExchangeException e) {
            log.error("거래소 마켓 조회 실패", e);
            throw e;
        }

        if (markets.isEmpty()) {
            log.warn("거래소에서 가져온 마켓 목록 비어 있음");
            throw new NotFoundException("지원 가능한 코인 목록이 없습니다");
        }

        List<Coin> newCoins = markets.stream()
                .map(m -> Coin.builder()
                        .symbol(m.symbol())
                        .name(m.name())
                        .market(m.market())
                        .build())
                .toList();

        coinRepository.saveAll(newCoins);

        return newCoins.stream().map(CoinResponse::from).toList();
    }
}
