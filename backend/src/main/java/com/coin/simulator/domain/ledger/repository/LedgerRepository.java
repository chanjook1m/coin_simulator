package com.coin.simulator.domain.ledger.repository;

import com.coin.simulator.domain.ledger.entity.Ledger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Page<Ledger> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("select coalesce(sum(l.changeAmount), 0) " +
            "from Ledger l where l.user.id = :userId and l.asset = 'KRW'")
    BigDecimal sumCashByUserId(@Param("userId") Long userId);
}
