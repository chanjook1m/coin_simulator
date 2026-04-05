package com.coin.simulator.domain.ledger.repository;

import com.coin.simulator.domain.ledger.entity.Ledger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Page<Ledger> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
