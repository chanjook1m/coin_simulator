package com.coin.simulator.domain.wallet.entity;

import com.coin.simulator.domain.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal balance;
}
