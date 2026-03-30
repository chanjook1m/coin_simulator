package com.coin.simulator.domain.price.repository;

import com.coin.simulator.domain.coin.entity.Coin;
import com.coin.simulator.domain.price.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {
    Optional<Price> findTopByCoinOrderByTimestampDesc(Coin coin);
}
