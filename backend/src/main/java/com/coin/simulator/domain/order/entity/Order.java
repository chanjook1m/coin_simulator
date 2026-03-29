package com.coin.simulator.domain.order.entity;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coin coin;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderMethod method;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal targetPrice;
    private BigDecimal totalQuantity;
    private BigDecimal filledQuantity;
    private LocalDateTime createdAt;

    public static Order marketBuy(User user, Coin coin, BigDecimal executedPrice, BigDecimal quantity) {
        return Order.builder()
                .user(user)
                .coin(coin)
                .type(OrderType.MARKET)
                .method(OrderMethod.BUY)
                .status(OrderStatus.FILLED)
                .targetPrice(executedPrice)
                .totalQuantity(quantity)
                .filledQuantity(quantity)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Order limitBuyPending(User user, Coin coin, BigDecimal limitPrice, BigDecimal quantity) {
        return Order.builder()
                .user(user)
                .coin(coin)
                .type(OrderType.LIMIT)
                .method(OrderMethod.BUY)
                .status(OrderStatus.FILLED)
                .targetPrice(limitPrice)
                .totalQuantity(quantity)
                .filledQuantity(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
