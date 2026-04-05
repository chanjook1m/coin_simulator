package com.coin.simulator.domain.holding.service;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.holding.entity.Holding;
import com.coin.simulator.domain.holding.exception.HoldingNotFoundException;
import com.coin.simulator.domain.holding.repository.HoldingRepository;
import com.coin.simulator.domain.wallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HoldingService {
    private final HoldingRepository holdingRepository;


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
    }

    @Transactional
    public void decreaseCoin(Wallet wallet, Coin coin, BigDecimal quantity) {
        Holding holding = holdingRepository
                .findByWalletIdAndCoinId(wallet.getId(), coin.getId())
                .orElseThrow(() -> new HoldingNotFoundException(wallet.getId(), coin.getId()));

        holding.decreaseQuantity(quantity);
    }

}
