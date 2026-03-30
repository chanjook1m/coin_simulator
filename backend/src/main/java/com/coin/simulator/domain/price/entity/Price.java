package com.coin.simulator.domain.price.entity;

import com.coin.simulator.domain.coin.entity.Coin;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;

    private BigDecimal price;
    private LocalDateTime timestamp;

    @Builder
    public Price(Coin coin, BigDecimal price, LocalDateTime timestamp) {
        this.coin = coin;
        this.price = price;
        this.timestamp = timestamp;
    }

    public static Price of(Coin coin, BigDecimal price, LocalDateTime timestamp) {
        return Price.builder()
                .coin(coin)
                .price(price)
                .timestamp(timestamp)
                .build();
    }
}
