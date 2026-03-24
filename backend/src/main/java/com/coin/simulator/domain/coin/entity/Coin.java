package com.coin.simulator.domain.coin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String name;
    private String market;

    @Enumerated(EnumType.STRING)
    private CoinStatus coinStatus;

    @Builder
    public Coin(String symbol, String name, String market) {
        this.symbol = symbol;
        this.name = name;
        this.market = market;
    }
}
