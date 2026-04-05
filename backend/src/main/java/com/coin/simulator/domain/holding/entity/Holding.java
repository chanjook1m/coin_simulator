package com.coin.simulator.domain.holding.entity;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.holding.exception.InsufficientCoinQuantityException;
import com.coin.simulator.domain.wallet.entity.Wallet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @Column(precision = 30, scale = 8)
    private BigDecimal quantity;
    
    private BigDecimal avgPrice;

    @Builder // 빌더 패턴 적용을 위한 생성자
    public Holding(Wallet wallet, Coin coin, BigDecimal quantity, BigDecimal avgPrice) {
        this.wallet = wallet;
        this.coin = coin;
        this.quantity = quantity;
        this.avgPrice = avgPrice;
    }

    public void checkEnough(BigDecimal quantity) {
        if (this.quantity.compareTo(quantity) < 0) {
            throw new InsufficientCoinQuantityException(wallet.getUser().getId(), coin.getSymbol());
        }
    }

    public void increaseQuantity(BigDecimal quantity) {

        this.quantity = this.quantity.add(quantity);
    }

    public void decreaseQuantity(BigDecimal quantity) {
        log.info("HOLDING_DECREASE before={}, dec={}", this.quantity, quantity);
        checkEnough(quantity);
        this.quantity = this.quantity.subtract(quantity);
        log.info("HOLDING_DECREASE after={}", this.quantity);
    }
}
