package com.coin.simulator.domain.holding.entity;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.wallet.entity.Wallet;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coin_id")
    private Coin coin;

    private BigDecimal quantity;
    private BigDecimal avgPrice;
}
