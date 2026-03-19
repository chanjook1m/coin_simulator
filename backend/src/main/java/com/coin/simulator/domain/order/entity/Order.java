package com.coin.simulator.domain.order.entity;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    private Coin coin;

    private String type;
    private String method;
    private BigDecimal targetPrice;
    private BigDecimal totalQuantity;
    private BigDecimal filledQuantity;
    private String status;
    private LocalDateTime createdAt;
}
