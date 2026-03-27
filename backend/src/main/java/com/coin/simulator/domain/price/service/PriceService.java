package com.coin.simulator.domain.price.service;

import com.coin.simulator.common.exception.BadRequestException;
import com.coin.simulator.common.exception.NotFoundException;
import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.coin.repository.CoinRepository;
import com.coin.simulator.domain.price.dto.PriceResponse;
import com.coin.simulator.domain.price.entity.Price;
import com.coin.simulator.domain.price.repository.PriceRepository;
import com.coin.simulator.infrastructure.exchange.ExchangeClient;
import com.coin.simulator.infrastructure.exchange.dto.ExchangeTickerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceService {
    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");
    private final PriceRepository priceRepository;
    private final CoinRepository coinRepository;
    private final ExchangeClient exchangeClient;

    public PriceResponse getLatestPrice(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new BadRequestException("심볼은 필수 값입니다");
        }

        String upperSymbol = symbol.toUpperCase();

        Coin coin = coinRepository.findBySymbol(upperSymbol)
                .orElseThrow(() -> new NotFoundException("존재 하지 않는 코인입니다" + upperSymbol));

        PriceResponse latestPrice = fetchFromExchangeAndSave(coin);

        return latestPrice;
    }

    private PriceResponse fetchFromExchangeAndSave(Coin coin) {
        String market = "KRW-" + coin.getSymbol();

        List<ExchangeTickerResponse> tickers;

        tickers = exchangeClient.getTicker(List.of(market));

        if (tickers == null || tickers.isEmpty()) {
            log.error("거래소에서 가져온 코인 시세 비어있음");
            throw new NotFoundException("해당 코인 시세를 찾을 수 없습니다: " + coin.getSymbol());
        }

        ExchangeTickerResponse firstTicker = tickers.get(0);
        LocalDateTime ts = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(firstTicker.timestampMillis()), SEOUL_ZONE);

        Price price = Price.of(
                coin,
                firstTicker.price(),
                ts);

        priceRepository.save(price);

        return PriceResponse.from(price);
    }
}
