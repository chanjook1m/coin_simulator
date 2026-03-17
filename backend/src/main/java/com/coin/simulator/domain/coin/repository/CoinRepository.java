package com.coin.simulator.domain.coin.repository;

import com.coin.simulator.domain.coin.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findBySymbol(String symbol);
}
