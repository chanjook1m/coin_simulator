package com.coin.simulator.domain.price.entity;

import com.coin.simulator.domain.coin.entity.Coin;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;

    private BigDecimal price;
    private LocalDateTime timestamp;
}
