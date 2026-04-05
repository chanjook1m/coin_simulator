package com.coin.simulator.domain.holding.service;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.holding.dto.ActiveHoldingResponse;
import com.coin.simulator.domain.holding.entity.Holding;
import com.coin.simulator.domain.holding.exception.HoldingNotFoundException;
import com.coin.simulator.domain.holding.repository.HoldingRepository;
import com.coin.simulator.domain.price.dto.PriceResponse;
import com.coin.simulator.domain.price.service.PriceService;
import com.coin.simulator.domain.wallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HoldingService {
    private final HoldingRepository holdingRepository;
    private final PriceService priceService;

    @Transactional(readOnly = true)
    public void checkEnoughCoin(Wallet wallet, Coin coin, BigDecimal quantity) {
        Holding holding = holdingRepository
                .findByWalletIdAndCoinId(wallet.getId(), coin.getId())
                .orElseThrow(() -> new HoldingNotFoundException(wallet.getId(), coin.getId()));

        holding.checkEnough(quantity);
    }

    @Transactional
    public void increaseCoin(Wallet wallet, Coin coin, BigDecimal quantity, BigDecimal currentPrice) {
        Holding holding = holdingRepository
                .findByWalletIdAndCoinId(wallet.getId(), coin.getId())
                .orElse(null);

        if (holding == null) {
            holding = Holding.builder()
                    .wallet(wallet)
                    .coin(coin)
                    .quantity(quantity)
                    .avgPrice(currentPrice)
                    .build();
            // avgPrice는 추후 설계에 따라 처리 (가중평균 등)
            holdingRepository.save(holding);
        } else {
            holding.increaseQuantity(quantity);
        }
        log.info("[INC] walletId={}, coinId={}, incQty={}", wallet.getId(), coin.getId(), quantity);
    }

    @Transactional
    public void decreaseCoin(Wallet wallet, Coin coin, BigDecimal quantity) {
        Holding holding = holdingRepository
                .findByWalletIdAndCoinId(wallet.getId(), coin.getId())
                .orElseThrow(() -> new HoldingNotFoundException(wallet.getId(), coin.getId()));

        holding.decreaseQuantity(quantity);
        log.info("[DEC] walletId={}, coinId={}, decQty={}", wallet.getId(), coin.getId(), quantity);
    }

    @Transactional
    public List<ActiveHoldingResponse> getActiveHoldings(Long userId) {
        List<Holding> holdings = holdingRepository.findByWalletUserId(userId);

        List<ActiveHoldingResponse> result = new ArrayList<>();

        for (Holding holding : holdings) {
            BigDecimal quantity = holding.getQuantity();
            if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
                continue; // 수량 0 이하는 스킵
            }

            String symbol = holding.getCoin().getSymbol();
            String name = holding.getCoin().getName();
            BigDecimal avgPrice = holding.getAvgPrice();

            PriceResponse price = priceService.getLatestPrice(symbol);
            BigDecimal currentPrice = price.price();

            BigDecimal valuation = currentPrice.multiply(quantity);
            BigDecimal floatingPnl = currentPrice
                    .subtract(avgPrice)
                    .multiply(quantity);

            result.add(new ActiveHoldingResponse(
                    holding.getId(),
                    symbol,
                    name,
                    quantity,
                    avgPrice,
                    currentPrice,
                    valuation,
                    floatingPnl
            ));
        }

        return result;
    }

}
