package com.coin.simulator.domain.holding.repository;

import com.coin.simulator.domain.holding.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    Optional<Holding> findByWalletIdAndCoinId(Long walletId, Long coindId);

    List<Holding> findByWalletUserId(Long userId);
}
